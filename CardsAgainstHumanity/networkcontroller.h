#ifndef NETWORKCONTROLLER_H
#define NETWORKCONTROLLER_H

#include <QObject>

class NetworkController : public QObject
{
    Q_OBJECT
public:
    explicit NetworkController(QObject *parent = nullptr);



signals:
    void successfullyLoggedIn();
    void successfullyRegistered();
    void successfullyJoinedRoom(QString name);
    void roomListReceived(QStringList);
    void roomDataReceived(QStringList players, QString czarname, int rounds);
    void connectionLost();
    void gameStarted();
    void gameState(QString state, int round, int maxrounds);
    void cardsReceived(QString blackCard, QStringList cards);
    void picksReceived(QStringList picks, QStringList users);


public slots:
    void login(QString uname, QString pw);
    void registeruser(QString uname, QString email, QString password);
    void createRoom(QString name);
    void joinRoom(QString room);
    void leaveRoom();
    void startGame(int rounds);
    void kickPlayer(QString name);
    void invitePlayer(QString name);
    void getRoomData();
    void getRoomList();
    void sendPickedCards(QStringList cards);
    void sendVote(QString vote);
    void updateGameState();
    void getCards();
    void getPicks();
};

#endif // NETWORKCONTROLLER_H
