#ifndef NETWORKCONTROLLER_H
#define NETWORKCONTROLLER_H

#include <QObject>
#include <QNetworkAccessManager>
#include <QNetworkReply>
#include <QJsonDocument>
#include <QJsonObject>
#include <QJsonArray>

class NetworkController : public QObject
{
    Q_OBJECT
public:
    explicit NetworkController(QObject *parent = nullptr);


private:
    QNetworkAccessManager* manager;
    QString loggedInUsername;
    QString loggedInPassword;
    QString joinedRoomId;

signals:
    void successfullyLoggedIn();
    void successfullyRegistered();
    void successfullyJoinedRoom(QString name);
    void roomListReceived(QStringList);
    void roomDataReceived(QStringList players, QString czarname, int rounds, bool isplayerczar);
    void connectionLost();
    void gameStarted();
    void gameState(QString state, int round, int maxrounds);
    void cardsReceived(QString blackCard, QStringList cards, int picks);
    void picksReceived(QStringList picks, QStringList users, int numberOfPicks);


public slots:
    void login(QString uname, QString pw);//
    void registeruser(QString uname, QString email, QString password);//
    void createRoom(QString name);//
    void joinRoom(QString room);//
    void leaveRoom();//
    void startGame(int rounds);//
    void kickPlayer(QString name);//
    void invitePlayer(QString name);////ez mi
    void getRoomData();//
    void getRoomList();//
    void sendPickedCards(QStringList cards);
    void sendVote(QString vote);
    void updateGameState();//
    void getCards();//
    void getPicks();

    void handleLoginResult(QNetworkReply *reply);
    void handleRegisterResult(QNetworkReply *reply);
    void handleRoomListResult(QNetworkReply *reply);
    void handleRoomCreateResult(QNetworkReply *reply);
    void handleRoomJoinResult(QNetworkReply *reply);
    void handleGameStartResult(QNetworkReply *reply);
    void handlePlayerKickResult(QNetworkReply *reply);
    void handleRoomDataResult(QNetworkReply *reply);
    void handleCardsResult(QNetworkReply *reply);
    void handleGamestateResult(QNetworkReply *reply);




};

#endif // NETWORKCONTROLLER_H
