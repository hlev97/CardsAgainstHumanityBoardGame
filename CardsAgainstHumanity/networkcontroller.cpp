#include "networkcontroller.h"

NetworkController::NetworkController(QObject *parent)
    : QObject{parent}
{

}

void NetworkController::login(QString uname, QString pw)
{
    emit successfullyLoggedIn();
}

void NetworkController::registeruser(QString uname, QString email, QString password)
{

}

void NetworkController::createRoom(QString name)
{

}

void NetworkController::joinRoom(QString room)
{

}

void NetworkController::leaveRoom()
{

}

void NetworkController::startGame(int rounds)
{

}

void NetworkController::kickPlayer(QString name)
{

}

void NetworkController::invitePlayer(QString name)
{

}

void NetworkController::getRoomData()
{

}

void NetworkController::getRoomList()
{

}

void NetworkController::sendPickedCards(QStringList cards)
{

}

void NetworkController::sendVote(QString vote)
{

}

void NetworkController::updateGameState()
{

}

void NetworkController::getCards()
{

}

void NetworkController::getPicks()
{

}
