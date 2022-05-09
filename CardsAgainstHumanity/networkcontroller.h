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
};

#endif // NETWORKCONTROLLER_H
