#include "roomlistitem.h"
#include "ui_roomlistitem.h"

RoomListItem::RoomListItem(QString name, QString roomid, QWidget *parent) :
    QWidget(parent),
    ui(new Ui::RoomListItem)
{
    id = roomid;
    ui->setupUi(this);
    ui->text->setText(name);
    QObject::connect(ui->connectButton, &QPushButton::clicked, this, &RoomListItem::ConnectButtonClicked);
}

void RoomListItem::ConnectButtonClicked(bool checked){
    emit RoomSelected(id);
}

RoomListItem::~RoomListItem()
{
    delete ui;
}
