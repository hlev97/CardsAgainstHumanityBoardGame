#include "networkcontroller.h"

NetworkController::NetworkController(QObject *parent)
    : QObject{parent}
{
    manager = new QNetworkAccessManager(this);
}

void NetworkController::login(QString uname, QString pw)
{
    loggedInUsername = uname;
    loggedInPassword = pw;

    //emit successfullyLoggedIn();
    QString concatenated = uname + ":" + pw;
    QByteArray data = concatenated.toLocal8Bit().toBase64();
    QString headerData = "Basic " + data;
    QNetworkRequest request(QUrl("http://localhost:8080/users/me"));
    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handleLoginResult(QNetworkReply*)));
    manager->get(request);

}

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
void NetworkController::createRoom(QString name)
{
    QNetworkRequest request(QUrl("http://localhost:8080/api/room"));
    QString concatenated = loggedInUsername + ":" + loggedInPassword;
    QByteArray data = concatenated.toLocal8Bit().toBase64();
    QString headerData = "Basic " + data;

    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");

    QJsonObject obj;
    obj["roomId"] = name;

    QJsonDocument doc(obj);

    QByteArray body = doc.toJson();

    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handleRoomCreateResult(QNetworkReply*)));
    manager->post(request, body);
    //qDebug() << body;
}

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

void NetworkController::joinRoom(QString room)
{

    QNetworkRequest request(QUrl("http://localhost:8080/api/room/"+room+"/join"));
    QString concatenated = loggedInUsername + ":" + loggedInPassword;
    QByteArray data = concatenated.toLocal8Bit().toBase64();
    QString headerData = "Basic " + data;

    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
//    request.setRawHeader("Authorization", headerData.toLocal8Bit());

    QJsonObject obj;


    QJsonDocument doc(obj);
    QByteArray body = doc.toJson();

    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handleRoomJoinResult(QNetworkReply*)));
    manager->put(request, body);
    //qDebug() << body;

}

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
void NetworkController::leaveRoom()
{
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/"+joinedRoomId+"/join"));
    QString concatenated = loggedInUsername + ":" + loggedInPassword;
    QByteArray data = concatenated.toLocal8Bit().toBase64();
    QString headerData = "Basic " + data;

    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
    manager->deleteResource(request);
    //qDebug() << body;
}

/*void NetworkController::handleRoomLeaveResult(QNetworkReply *reply)
{
    QObject::disconnect(manager, SIGNAL(finished(QNetworkReply*)), 0, 0);
    if (reply->error()) {
        qDebug() << reply->errorString();
        return;
    }

    QByteArray response_data = reply->readAll();
    QJsonDocument json = QJsonDocument::fromJson(response_data);
    QString roomId = QString::number(json["roomId"].toDouble());
    qDebug() << "left room: " << roomId;
    joinedRoomId = "";

}*/

void NetworkController::startGame(int rounds)
{
        QString concatenated = loggedInUsername + ":" + loggedInPassword;
        QByteArray data = concatenated.toLocal8Bit().toBase64();
        QString headerData = "Basic " + data;
        QNetworkRequest request(QUrl("http://localhost:8080/api/room/"+joinedRoomId+"/initGame"));
        request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
        request.setRawHeader("Authorization", headerData.toLocal8Bit());


        QJsonObject obj;
        obj["rounds"] = rounds;


        QJsonDocument doc(obj);
        QByteArray body = doc.toJson();

        QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handleGameStartResult(QNetworkReply*)));
        manager->put(request, body);
        //qDebug() << body;
}

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

void NetworkController::kickPlayer(QString name)
{
    QString concatenated = loggedInUsername + ":" + loggedInPassword;
    QByteArray data = concatenated.toLocal8Bit().toBase64();
    QString headerData = "Basic " + data;
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/"+joinedRoomId+"/kick/"+name));
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
    request.setRawHeader("Authorization", headerData.toLocal8Bit());

    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handlePlayerKickResult(QNetworkReply*)));
    manager->deleteResource(request);
    //qDebug() << body;
}

void NetworkController::handlePlayerKickResult(QNetworkReply *reply)
{
    QObject::disconnect(manager, SIGNAL(finished(QNetworkReply*)), 0, 0);
    if (reply->error()) {
        qDebug() << reply->errorString();
        return;
    }
    qDebug() << "kicked player";

}

void NetworkController::invitePlayer(QString name)
{

}

void NetworkController::getRoomData()
{
    QString concatenated = loggedInUsername + ":" + loggedInPassword;
    QByteArray data = concatenated.toLocal8Bit().toBase64();
    QString headerData = "Basic " + data;
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/"+joinedRoomId));
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handleRoomDataResult(QNetworkReply*)));
    manager->get(request);
    //qDebug() << body;
}

void NetworkController::handleRoomDataResult(QNetworkReply *reply)
{
    QObject::disconnect(manager, SIGNAL(finished(QNetworkReply*)), 0, 0);
    QList<QString> playerList;
    QString czarName;
    int rounds;

    if (reply->error()) {
        qDebug() << reply->errorString();
        return;
    }

    QByteArray response_data = reply->readAll();

    QJsonDocument json = QJsonDocument::fromJson(response_data);

    QJsonArray jsonArray = json["connectedUsers"].toArray();
    for (auto v : jsonArray) {
        playerList.append(v.toString());
        //qDebug() << v.toString();
    }

    czarName = json["czarId"].toString();
    rounds = json["rounds"].toInt();
    //qDebug() << "registered and logged in as: " << loggedInUsername;
    bool isCzar = QString::compare(czarName, loggedInUsername) == 0;
    emit roomDataReceived(playerList, czarName, rounds, isCzar);
}

void NetworkController::getRoomList()
{
    QString concatenated = loggedInUsername + ":" + loggedInPassword;
    QByteArray data = concatenated.toLocal8Bit().toBase64();
    QString headerData = "Basic " + data;
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/list"));
    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handleRoomListResult(QNetworkReply*)));
    manager->get(request);
}

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

void NetworkController::sendPickedCards(QList<int> cards)
{
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/" + joinedRoomId + "/chooseCards"));
    QString concatenated = loggedInUsername + ":" + loggedInPassword;
    QByteArray data = concatenated.toLocal8Bit().toBase64();
    QString headerData = "Basic " + data;

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
    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handleRoomListResult(QNetworkReply*)));

    manager->put(request, body);
}

void NetworkController::sendVote(QString vote)
{
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/" + joinedRoomId + "/voteCards"));
    QString concatenated = loggedInUsername + ":" + loggedInPassword;
    QByteArray data = concatenated.toLocal8Bit().toBase64();
    QString headerData = "Basic " + data;

    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");


    QJsonObject obj;
    obj["user"] = vote;
    QJsonDocument doc(obj);
    QByteArray body = doc.toJson();
    qDebug() << obj;

    //QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handleGamestateResult(QNetworkReply*)));

    manager->put(request, body);
}

void NetworkController::updateGameState()
{
    QString concatenated = loggedInUsername + ":" + loggedInPassword;
    QByteArray data = concatenated.toLocal8Bit().toBase64();
    QString headerData = "Basic " + data;
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/" + joinedRoomId + "/gameState"));
    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handleGamestateResult(QNetworkReply*)));
    manager->get(request);
}
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

void NetworkController::getCards()
{
    QString concatenated = loggedInUsername + ":" + loggedInPassword;
    QByteArray data = concatenated.toLocal8Bit().toBase64();
    QString headerData = "Basic " + data;
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/" + joinedRoomId + "/gameState"));
    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handleCardsResult(QNetworkReply*)));
    manager->get(request);

}

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


    //qDebug() << "registered and logged in as: " << loggedInUsername;
    emit cardsReceived(blackCard, whiteCards, cardIds, numberOfPicks);
}

void NetworkController::getPicks()
{
    QString concatenated = loggedInUsername + ":" + loggedInPassword;
    QByteArray data = concatenated.toLocal8Bit().toBase64();
    QString headerData = "Basic " + data;
    QNetworkRequest request(QUrl("http://localhost:8080/api/room/" + joinedRoomId + "/gameState"));
    request.setRawHeader("Authorization", headerData.toLocal8Bit());
    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(handlePicksResult(QNetworkReply*)));
    manager->get(request);
}
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
        //qDebug() << "Key = " << key;

        users.append(key);
        for(auto card: value.toArray())
        {
            picks.append(card.toObject()["text"].toString());
        }
    }

    numberOfPicks = doc["black"].toObject()["pick"].toInt();


    emit picksReceived(picks, users, numberOfPicks);

}

