import QtQuick 2.0
import QtQuick.Controls 6.2

Item {

    width: 640
    height: 480

    signal showLoginView()
    signal showRegisterView()
    signal showMainMenuView()
    signal showGameView()

    Component.onCompleted: {
        nc.updateGameState();
    }

    Connections{
        target: nc
        property string currentstate : ""

        function onGameState(state, round, maxround) {
            polling.restart();
            if(currentstate === state)
                return;
            lrounds.text = "Rounds: " + round + "/" + maxround;
            switch(state){
            case "pick":
                nc.getWhiteCards();
                break;
            case "vote":
                nc.getPicks();
                break;
            default:
            }
        }

        function onCardsReceived(blackcard, cards){
            lplayerwhitecards.clear();
            cards.forEach(card => lplayerwhitecards.append({name : card}));
            lblackcard.text = blackcard;
        }

        function onPicksReceived(picks, users){

        }
    }

    Timer{
        id: polling
        running: false
        repeat: false
        interval: 1000
        onTriggered: nc.updateGameState();
    }

    Label {
        id: lrounds
        x: 400
        y: 24
        text: qsTr("Rounds: 1/5")
    }

    Label {
        id: label1
        x: 258
        y: 80
        text: qsTr("Black Card:")
    }

    Label {
        id: label2
        x: 258
        y: 218
        width: 59
        height: 16
        text: qsTr("Your Cards:")
    }

    Rectangle {
        id: rectangle
        x: 400
        y: 80
        width: 100
        height: 100
        color: "#000000"

        Label {
            id: lblackcard
            x: 10
            y: 10
            width: 80
            height: 80
            color: "#ffffff"
            text: qsTr("Label")
            horizontalAlignment: Text.AlignHCenter
            verticalAlignment: Text.AlignVCenter
        }
    }

    ScrollView{
        x: 250
        y: 240

        width: 310
        height: 150

        ListView {
            clip: true
            id: lwcards
            x: 250
            y: 240
            width: 300
            height: 150
            orientation: ListView.Horizontal
            flickableDirection: Flickable.HorizontalFlick
            boundsBehavior: Flickable.StopAtBounds
            property var picks: [];
            property int picknum : 2;
            delegate: Item {
                width: 110
                height: 100
                Rectangle {
                    x: 5
                    width: 100
                    height: 100
                    color: "#ffffff"

                    Label {
                        x: 10
                        y: 10
                        width: 80
                        height: 50
                        color: "#000000"
                        text: name
                        horizontalAlignment: Text.AlignHCenter
                        verticalAlignment: Text.AlignVCenter
                    }

                    CheckBox {
                        x: 40
                        y: 60
                        width: 20
                        height: 20
                        onClicked: {
                            if(checkState === Qt.Checked)
                                lwcards.picks.push(name);
                            else{
                                lwcards.picks.splice(lwcards.picks.indexOf(name), 1);
                            }

                            if(lwcards.picks.length !== lwcards.picknum){
                                bSendPicks.enabled = false;
                            }
                            else{
                                bSendPicks.enabled = true;
                            }
                        }
                    }
                }
            }
            model: ListModel {
                id: lplayerwhitecards
                ListElement {
                    name: "Grey"
                }

                ListElement {
                    name: "Red"
                }

                ListElement {
                    name: "asd"
                }
            }
        }
    }

    Button {
        id: bSendPicks
        x: 516
        y: 396
        text: qsTr("Send")
        onClicked: {
            nc.sendPickedCards(lwcards.picks);
        }
        enabled: false
    }

    Label {
        id: lpicknum
        x: 337
        y: 218
        text: qsTr("Pick 2")
    }

}


