#include "menuview.h"
#include "roomlistitem.h"
#include "ui_menuview.h"

MenuView::MenuView(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::MenuView)
{
    ui->setupUi(this);
    connect(ui->bsearch, &QPushButton::clicked, this, &MenuView::SearchButtonPressed);
    connect(ui->brefresh, &QPushButton::clicked, this, &MenuView::RefreshButtonPressed);
    connect(ui->bcreate, &QPushButton::clicked, this, &MenuView::NewRoomButtonPressed);
    displayRoomList();
    emit RefreshRoomList();
}

MenuView::~MenuView()
{
    delete ui;
}

void MenuView::RoomListReceived(QStringList roomnames)
{
    roomlist = roomnames;
    displayRoomList();
}

void MenuView::SuccessfulConnection(QString roomid)
{

}

void MenuView::SearchButtonPressed(bool)
{
    displayRoomList();
}

void MenuView::RefreshButtonPressed(bool)
{
    emit RefreshRoomList();
}

void MenuView::NewRoomButtonPressed(bool)
{
    emit CreateRoom(ui->troomame->text());
}

void MenuView::ConnectButtonPressed(QString str)
{
    emit JoinRoom(str);
}

void MenuView::displayRoomList() {
    ui->roomlist->clear();
    for(QString str : roomlist.filter(ui->tsearch->text(), Qt::CaseInsensitive)){
        QListWidgetItem* it = new QListWidgetItem();
        RoomListItem* rli = new RoomListItem(str, str);
        connect(rli, &RoomListItem::RoomSelected, this, &MenuView::ConnectButtonPressed);
        it->setSizeHint(rli->size());
        ui->roomlist->addItem(it);
        ui->roomlist->setItemWidget(it, rli);
    }
}
