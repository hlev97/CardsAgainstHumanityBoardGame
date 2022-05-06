#ifndef MENUVIEW_H
#define MENUVIEW_H

#include <QWidget>

namespace Ui {
class MenuView;
}

class MenuView : public QWidget
{
    Q_OBJECT
    QStringList roomlist;

public:
    explicit MenuView(QWidget *parent = nullptr);
    ~MenuView();

signals:
    void ShowGameView();
    void JoinRoom(QString name);
    void CreateRoom(QString name);
    void RefreshRoomList();

private:
    Ui::MenuView *ui;
    void displayRoomList();

public slots:
    void RoomListReceived(QStringList roomnames);
    void SuccessfulConnection(QString name);

private slots:
    void SearchButtonPressed(bool);
    void RefreshButtonPressed(bool);
    void NewRoomButtonPressed(bool);
    void ConnectButtonPressed(QString);
};

#endif // MENUVIEW_H
