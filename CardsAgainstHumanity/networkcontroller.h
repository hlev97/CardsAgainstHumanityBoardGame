#ifndef NETWORKCONTROLLER_H
#define NETWORKCONTROLLER_H

#include <QObject>

class NetworkController : public QObject
{
    Q_OBJECT
public:
    explicit NetworkController(QObject *parent = nullptr);



signals:
    void RoomListReceived(QStringList);
    void ConnectionLost();


public slots:
    void Login(QString uname, QString pw);
    void GetRoomList();

};

#endif // NETWORKCONTROLLER_H
