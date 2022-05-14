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
    ~NetworkController();

    QString getHeaderData(QString uname, QString pw);       //returns a header for basic authorization


private:
    QNetworkAccessManager* manager;     //used for REST API requests
    QString loggedInUsername;           //the username of the account that we are logged in as
    QString loggedInPassword;           //the password of the account that we are logged in as
    QString joinedRoomId;               //the name of the room that we are currently connected to

signals:
    void successfullyLoggedIn();        //signals successfull login at the LoginView
    void successfullyRegistered();      //signals successfull registration at the LoginView
    void successfullyJoinedRoom(QString name);      //signals successfull room join at the MainMenuView
    void roomListReceived(QStringList);             //signals that the list of rooms is received at the MainMenuView
    void roomDataReceived(QStringList players, QString czarname, int rounds, bool isplayerczar, bool isPlayerKicked);        //signals that the room's data is received at the MainMenuView
    void gameStarted();         //signals that the game has started at the MainMenuView
    void gameState(QString state, int round, int maxrounds, QStringList users, QList<int> scores);      //signals that the gameState is received at the GameView
    void cardsReceived(QString blackCard, QStringList cards, QList<int> cardIds, int picks);        //signals that the cards in our hands are received at the GameView
    void picksReceived(QStringList picks, QStringList users, int numberOfPicks);        //signals that the cards picked by others are received at the GameView


public slots:
    void login(QString uname, QString pw);      //checks if a username + password pair is correct
    void registeruser(QString uname, QString email, QString password);      //registers a user with the given parameters
    void createRoom(QString name);      //creates a room with the given name
    void joinRoom(QString room);        //joins a room with the given name
    void leaveRoom();       //leaves the currently joined room
    void startGame(int rounds);     //starts the game of the currently joined room, with the given rounds number
    void kickPlayer(QString name);      //kicks the given player from the currently joined room
    void invitePlayer(QString name);        //invites a player via email to the currently joined room
    void getRoomData();     //gets the data of the currently joined room
    void getRoomList();     //gets the list of the rooms
    void sendPickedCards(QList<int> cards);     //sends the given cards for picking
    void sendVote(QString vote);        //sends the vote for the given user
    void updateGameState();     //gets the gameState of the currently joined room
    void getCards();        //gets the cards in the player's hand
    void getPicks();        //get the cards picked by others

    void handleLoginResult(QNetworkReply *reply);       //handles the REST API answer for login(...)
    void handleRegisterResult(QNetworkReply *reply);        //handles the REST API answer for registeruser(...)
    void handleRoomListResult(QNetworkReply *reply);        //handles the REST API answer for getRoomList(...)
    void handleRoomCreateResult(QNetworkReply *reply);      //handles the REST API answer for createRoom(...)
    void handleRoomJoinResult(QNetworkReply *reply);        //handles the REST API answer for joinRoom(...)
    void handleGameStartResult(QNetworkReply *reply);       //handles the REST API answer for startGame(...)
    void handleRoomDataResult(QNetworkReply *reply);        //handles the REST API answer for getRoomData(...)
    void handleCardsResult(QNetworkReply *reply);       //handles the REST API answer for getCards(...)
    void handleGamestateResult(QNetworkReply *reply);       //handles the REST API answer for updateGameState(...)
    void handlePicksResult(QNetworkReply *reply);       //handles the REST API answer for getPicks(...)

};

#endif // NETWORKCONTROLLER_H
