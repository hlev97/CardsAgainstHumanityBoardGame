#ifndef ROOMLISTITEM_H
#define ROOMLISTITEM_H

#include <QWidget>

namespace Ui {
class RoomListItem;
}

class RoomListItem : public QWidget
{
    Q_OBJECT

public:
    explicit RoomListItem(QString name, QString roomid, QWidget *parent = nullptr);
    ~RoomListItem();

signals:
    void RoomSelected(QString roomid);

private:
    Ui::RoomListItem *ui;
    QString id;

private slots:
    void ConnectButtonClicked(bool checked);
};

#endif // ROOMLISTITEM_H
