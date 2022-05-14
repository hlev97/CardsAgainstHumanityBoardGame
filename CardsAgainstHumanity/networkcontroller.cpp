#include "networkcontroller.h"

NetworkController::NetworkController(QObject *parent)
    : QObject{parent}
{
    manager = new QNetworkAccessManager(this);
}

NetworkController::~NetworkController(){
    delete manager;
}

/**
* Get header that contains authorization information
* @param uname username
* @param pw password
* @return header
*/
QString NetworkController::getHeaderData(QString uname, QString pw)
{
    QString concatenated = uname + ":" + pw;
    QByteArray data = concatenated.toLocal8Bit().toBase64();
    QString headerData = "Basic " + data;
    return headerData;
}

/**
* Checks if a username + password pair is correct
* @param uname username
* @param pw password
*/
void NetworkController::login(QString uname, QString pw)
{
    loggedInUsername = uname;
    loggedInPassword = pw;
    QString headerData = getHeaderData(uname, pw);
    QNetworkRequest request(QUrl("http://localhost:8080/users/me"));
    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handleLoginResult(QNetworkReply*)));
    manager->get(request);
}

/**
* Emits successfullyLoggedIn() signal based on the server response
* @param reply reply
*/
void NetworkController::handleLoginResult(QNetworkReply *reply){

    QObject::disconnect(manager, SIGNAL(finished(QNetworkReply*)), 0, 0);
    if (reply->error()) {
        loggedInUsername = "";
        loggedInPassword = "";
        qDebug() << reply->errorString();
        return;
    }
    qDebug() << "logged in as: " << loggedInUsername;
    emit successfullyLoggedIn();
}

/**
* Registers a user with the given parameters
* @param uname username
* @param email email
* @param password password
*/
void NetworkController::registeruser(QString uname, QString email, QString password)
{
        QNetworkRequest request(QUrl("http://localhost:8080/users"));
        request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
        loggedInUsername = uname;
        loggedInPassword = password;

        QJsonObject obj;
        obj["username"] = uname;
        obj["password"] = password;
        obj["email"] = email;

        QJsonDocument doc(obj);
        QByteArray body = doc.toJson();

        QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handleRegisterResult(QNetworkReply*)));
        manager->post(request, body);
}

/**
* Emits successfullyRegistered() signal based on the server response
* @param reply reply
*/
void NetworkController::handleRegisterResult(QNetworkReply *reply){
    QObject::disconnect(manager, SIGNAL(finished(QNetworkReply*)), 0, 0);
    if (reply->error()) {
        loggedInUsername = "";
        loggedInPassword = "";
        qDebug() << reply->errorString();
        return;
    }

    qDebug() << "registered and logged in as: " << loggedInUsername;
    emit successfullyRegistered();
}

/**
* Creates a room with the given name
* @param name roomname
*/
void NetworkController::createRoom(QString name)
{
    QNetworkRequest request(QUrl("http://localhost:8080/api/room"));
    QString headerData = getHeaderData(loggedInUsername, loggedInPassword);
    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");

    QJsonObject obj;
    obj["roomId"] = name;
    QJsonDocument doc(obj);
    QByteArray body = doc.toJson();
    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handleRoomCreateResult(QNetworkReply*)));
    manager->post(request, body);
}

/**
* Emits successfullyJoinedRoom() signal based on the server response
* @param reply reply
*/
void NetworkController::handleRoomCreateResult(QNetworkReply *reply){
    QObject::disconnect(manager, SIGNAL(finished(QNetworkReply*)), 0, 0);
    if (reply->error()) {
        qDebug() << reply->errorString();
        return;
    }

    QByteArray response_data = reply->readAll();
    QJsonDocument json = QJsonDocument::fromJson(response_data);
    QString roomId = json["roomId"].toString();
    qDebug() << "created room: " << roomId;
    joinedRoomId = roomId;
    emit successfullyJoinedRoom(roomId);
}

/**
* Joins a room with the given name
* @param name roomname
*/
void NetworkController::joinRoom(QString room)
{
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/"+room+"/join"));
    QString headerData = getHeaderData(loggedInUsername, loggedInPassword);

    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");

    QJsonObject obj;
    QJsonDocument doc(obj);
    QByteArray body = doc.toJson();

    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handleRoomJoinResult(QNetworkReply*)));
    manager->put(request, body);

}

/**
* Emits successfullyJoinedRoom() signal based on the server response
* @param reply reply
*/
void NetworkController::handleRoomJoinResult(QNetworkReply *reply)
{

    QObject::disconnect(manager, SIGNAL(finished(QNetworkReply*)), 0, 0);
    if (reply->error()) {
        qDebug() << reply->errorString();
        return;
    }

    QByteArray response_data = reply->readAll();
    QJsonDocument json = QJsonDocument::fromJson(response_data);
    QString roomId = json["roomId"].toString();
    qDebug() << "joined room: " << roomId;
    joinedRoomId = roomId;
    emit successfullyJoinedRoom(roomId);
}

/**
* Leaves the currently joined room
*/
void NetworkController::leaveRoom()
{
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/"+joinedRoomId+"/leave"));
    QString headerData = getHeaderData(loggedInUsername, loggedInPassword);

    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
    QJsonObject obj;
    QJsonDocument doc(obj);
    QByteArray body = doc.toJson();

    manager->put(request,body);
}


/**
* Starts the room with the given number of rounds
* @param rounds numberOfRounds
*/
void NetworkController::startGame(int rounds)
{
        QString headerData = getHeaderData(loggedInUsername, loggedInPassword);
        QNetworkRequest request(QUrl("http://localhost:8080/api/room/"+joinedRoomId+"/initGame"));
        request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
        request.setRawHeader("Authorization", headerData.toLocal8Bit());

        QJsonObject obj;
        obj["rounds"] = rounds;
        QJsonDocument doc(obj);
        QByteArray body = doc.toJson();

        QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handleGameStartResult(QNetworkReply*)));
        manager->put(request, body);
}

/**
* Emits gameStarted() signal based on the server response
* @param reply reply
*/
void NetworkController::handleGameStartResult(QNetworkReply *reply)
{
    QObject::disconnect(manager, SIGNAL(finished(QNetworkReply*)), 0, 0);
    if (reply->error()) {
        qDebug() << reply->errorString();
        return;
    }
    qDebug() << "room: " << joinedRoomId << " started";
    emit gameStarted();
}


/**
* kicks the given player from the currently joined room
* @param name name
*/
void NetworkController::kickPlayer(QString name)
{
    QString headerData = getHeaderData(loggedInUsername, loggedInPassword);
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/"+joinedRoomId+"/kick/"+name));
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
    request.setRawHeader("Authorization", headerData.toLocal8Bit());

    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handlePlayerKickResult(QNetworkReply*)));

    QJsonObject obj;
    QJsonDocument doc(obj);
    QByteArray body = doc.toJson();
    manager->put(request,body);
    //qDebug() << body;
}


/**
* Invites a player via email to the currently joined room
* @param name name
*/
void NetworkController::invitePlayer(QString name)
{
    if (joinedRoomId == "")
        return;

    QString headerData = getHeaderData(loggedInUsername, loggedInPassword);
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/"+joinedRoomId+"/invite/"+name));
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
    request.setRawHeader("Authorization", headerData.toLocal8Bit());

    manager->get(request);
}

/**
* Gets the data of the currently joined room
*/
void NetworkController::getRoomData()
{
    QString headerData = getHeaderData(loggedInUsername, loggedInPassword);
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/"+joinedRoomId));
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handleRoomDataResult(QNetworkReply*)));
    manager->get(request);
}

/**
* Emits roomDataReceived(...) signal based on the server response
* @param reply reply
*/
void NetworkController::handleRoomDataResult(QNetworkReply *reply)
{
    QObject::disconnect(manager, SIGNAL(finished(QNetworkReply*)), 0, 0);
    QList<QString> playerList;
    QString czarName;
    int rounds;
    bool started;
    bool isPlayerConnected = false;

    if (reply->error()) {
        qDebug() << reply->errorString();
        return;
    }

    QByteArray response_data = reply->readAll();

    QJsonDocument json = QJsonDocument::fromJson(response_data);

    QJsonArray jsonArray = json["connectedUsers"].toArray();
    for (auto v : jsonArray) {
        playerList.append(v.toString());
        if (v.toString() == loggedInUsername)
            isPlayerConnected = true;

        //qDebug() << v.toString();
    }


    started = json["startedRoom"].toBool();
    if (started && isPlayerConnected){
        emit gameStarted();
        return;
    }



    czarName = json["czarId"].toString();
    rounds = json["rounds"].toInt();
    //qDebug() << "registered and logged in as: " << loggedInUsername;
    bool isCzar = QString::compare(czarName, loggedInUsername) == 0;
    emit roomDataReceived(playerList, czarName, rounds, isCzar, !isPlayerConnected);
}

/**
* Gets the list of the rooms
*/
void NetworkController::getRoomList()
{
    QString headerData = getHeaderData(loggedInUsername, loggedInPassword);
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/list"));
    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handleRoomListResult(QNetworkReply*)));
    manager->get(request);
}

/**
* Emits roomListReceived(...) signal based on the server response
* @param reply reply
*/
void NetworkController::handleRoomListResult(QNetworkReply *reply)
{
    QObject::disconnect(manager, SIGNAL(finished(QNetworkReply*)), 0, 0);
    QList<QString> nameList;
    if (reply->error()) {
        qDebug() << reply->errorString();
        return;
    }

    QByteArray response_data = reply->readAll();

    QJsonDocument json = QJsonDocument::fromJson(response_data);

    QJsonArray jsonArray = json.array();
    for (auto v : jsonArray) {
        QJsonObject element = v.toObject();
        //qDebug() << element["roomId"].toString();
        nameList.append(element["roomId"].toString());
    }

    emit roomListReceived(nameList);

}


/**
* Sends the given cards for picking
* @param cards cards
*/
void NetworkController::sendPickedCards(QList<int> cards)
{
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/" + joinedRoomId + "/chooseCards"));
    QString headerData = getHeaderData(loggedInUsername, loggedInPassword);

    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");

    QJsonArray jsonArray;

    for (auto v : cards) {
        jsonArray.append(v);
    }

    QJsonObject obj;
    obj["cards"] = jsonArray;
    QJsonDocument doc(obj);
    QByteArray body = doc.toJson();

    manager->put(request, body);
}

/**
* Sends the vote for the given user
* @param vote vote
*/
void NetworkController::sendVote(QString vote)
{
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/" + joinedRoomId + "/voteCards"));
    QString headerData = getHeaderData(loggedInUsername, loggedInPassword);

    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");


    QJsonObject obj;
    obj["user"] = vote;
    QJsonDocument doc(obj);
    QByteArray body = doc.toJson();
    qDebug() << obj;

    manager->put(request, body);
}

/**
* Gets the gameState of the currently joined room
*/
void NetworkController::updateGameState()
{
    QString headerData = getHeaderData(loggedInUsername, loggedInPassword);
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/" + joinedRoomId + "/gameState"));
    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handleGamestateResult(QNetworkReply*)));
    manager->get(request);
}

/**
* Emits gameState(...) signal based on the server response
* @param reply reply
*/
void NetworkController::handleGamestateResult(QNetworkReply *reply)
{
    QObject::disconnect(manager, SIGNAL(finished(QNetworkReply*)), 0, 0);

    if (reply->error()) {
        qDebug() << reply->errorString();
        return;
    }

    QByteArray response_data = reply->readAll();

    QJsonDocument json = QJsonDocument::fromJson(response_data);

    QString state = json["turnState"].toString();
    int round = json["currentRound"].toInt();
    int maxrounds = json["allRound"].toInt();
    QList<QString> users;
    QList<int> scores;

    QJsonDocument doc(QJsonDocument::fromJson(response_data));
    QJsonObject jsonScores = doc["scores"].toObject();
    foreach(const QString& key, jsonScores.keys()) {
        QJsonValue value = jsonScores.value(key);
        //qDebug() << "Key = " << key;

        users.append(key);
        scores.append(value.toInt());
    }


    emit gameState(state, round, maxrounds, users, scores);
}

/**
* Gets the cards in the player's hand
*/
void NetworkController::getCards()
{
    QString headerData = getHeaderData(loggedInUsername, loggedInPassword);
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/" + joinedRoomId + "/gameState"));
    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handleCardsResult(QNetworkReply*)));
    manager->get(request);
}

/**
* Emits cardsReceived(...) signal based on the server response
* @param reply reply
*/
void NetworkController::handleCardsResult(QNetworkReply *reply)
{
    QObject::disconnect(manager, SIGNAL(finished(QNetworkReply*)), 0, 0);
    QString blackCard;
    QList<QString> whiteCards;
    QList<int> cardIds;
    int numberOfPicks;
    if (reply->error()) {
        qDebug() << reply->errorString();
        return;
    }

    QByteArray response_data = reply->readAll();

    QJsonDocument json = QJsonDocument::fromJson(response_data);

    numberOfPicks = json["black"]["pick"].toDouble();
    blackCard = json["black"]["text"].toString();

        QJsonArray jsonArray = json["whites"].toArray();
        for (auto v : jsonArray) {
            QJsonObject element = v.toObject();

            whiteCards.append(element["text"].toString());
            cardIds.append(element["whiteId"].toInt());
        }
     emit cardsReceived(blackCard, whiteCards, cardIds, numberOfPicks);
}

/**
* Get the cards picked by others
*/
void NetworkController::getPicks()
{
    QString headerData = getHeaderData(loggedInUsername, loggedInPassword);
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/" + joinedRoomId + "/gameState"));
    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handlePicksResult(QNetworkReply*)));
    manager->get(request);
}

/**
* Emits picksReceived(...) signal based on the server response
* @param reply reply
*/
void NetworkController::handlePicksResult(QNetworkReply *reply)
{
    QObject::disconnect(manager, SIGNAL(finished(QNetworkReply*)), 0, 0);

    QStringList picks;
    QStringList users;
    int numberOfPicks;

    QByteArray response_data = reply->readAll();

    QJsonDocument doc(QJsonDocument::fromJson(response_data));
    QJsonObject json = doc["chosenCards"].toObject();
    foreach(const QString& key, json.keys()) {
        QJsonValue value = json.value(key);

        users.append(key);
        for(auto card: value.toArray())
        {
            picks.append(card.toObject()["text"].toString());
        }
    }
    numberOfPicks = doc["black"].toObject()["pick"].toInt();
    emit picksReceived(picks, users, numberOfPicks);
}

