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
            case "TURN_CHOOSING_CARDS":
                votepane.visible = false;
                pickpane.visible = true;
                bSendPicks.enabled = false;
                nc.getWhiteCards();
                break;
            case "TURN_VOTING":
                votepane.visible = true;
                pickpane.visible = false;
                bSendVote.enabled = false;
                nc.getPicks();
                break;
            default:
            }
        }

        function onCardsReceived(blackcard, cards, numpicks){
            lplayerwhitecards.clear();
            cards.forEach(card => lplayerwhitecards.append({name : card}));
            lblackcard.text = blackcard;
            lwcards.picknum = numpicks;
        }

        function onPicksReceived(picks, users){
            lvotecards.playerlist = users;
            lvotecards.answerlist = picks;
            lvotecardsmodel.clear();
            picks.forEach(card => lvotecardsmodel.append({name : card}));
        }
    }

    Timer{
        id: polling
        running: false
        repeat: false
        interval: 1000
        onTriggered: nc.updateGameState();
    }

    Pane {
        id: pickpane
        x: 180
        y: 0
        width: 460
        height: 480
        visible:true

        Label {
            id: label2
            x: 68
            y: 209
            width: 59
            height: 16
            text: qsTr("Your Cards:")
        }

        ScrollView{
            x: 60
            y: 230

            width: 310
            height: 110
            ScrollBar.vertical.policy: ScrollBar.AlwaysOff

            ListView {
                clip: true
                id: lwcards
                x: -9
                y: -9
                width: 300
                height: 100
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
            x: 326
            y: 387
            text: qsTr("Send")
            onClicked: {
                nc.sendPickedCards(lwcards.picks);
            }
            enabled: false
        }

        Label {
            id: lpicknum
            x: 147
            y: 209
            text: qsTr("Pick 2")
        }
    }

    Pane {
        id: votepane
        x: 180
        y: 0
        width: 460
        height: 480
        visible: false

        ScrollView {
            id: scrollView
            x: 60
            y: 230

            width: 310
            height: 110
            ScrollBar.vertical.policy: ScrollBar.AlwaysOff

            ListView {
                clip:true
                orientation: ListView.Horizontal
                flickableDirection: Flickable.HorizontalFlick
                boundsBehavior: Flickable.StopAtBounds
                id: lvotecards
                width: 110
                height: 160
                property var playerlist
                property var answerlist
                property var selectedAnswer: null
                property var selectedButton: null
                model: ListModel {
                    id: lvotecardsmodel
                    ListElement {
                        name: "Grey"
                        colorCode: "grey"
                    }

                    ListElement {
                        name: "Red"
                        colorCode: "red"
                    }

                    ListElement {
                        name: "Blue"
                        colorCode: "blue"
                    }

                    ListElement {
                        name: "Green"
                        colorCode: "green"
                    }
                }
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
                                if(checkState === Qt.Checked){
                                    if(lvotecards.selectedButton !== null){
                                        lvotecards.selectedButton.checked = false;
                                    }
                                    lvotecards.selectedButton = this;
                                    lvotecards.selectedAnswer = name;
                                    bSendVote.enabled = true;
                                }
                                else{
                                    lvotecards.selectedButton = null;
                                    bSendVote.enabled = false;
                                }
                            }
                        }
                    }
                }
            }
        }

        Label {
            id: label
            x: 33
            y: 223
            text: qsTr("Vote for the funniest card:")
        }

        Button {
            enabled: false
            id: bSendVote
            x: 326
            y: 375
            text: qsTr("Send Vote")
            onClicked: nc.sendVote(lvotecards.playerlist[lvotecards.answerlist.indexOf(lvotecards.selectedAnswer)]);
        }
    }

    Label {
        id: lrounds
        x: 210
        y: 15
        text: qsTr("Rounds: 1/5")
    }

    Label {
        id: label1
        x: 210
        y: 70
        text: qsTr("Black Card:")
    }

    Rectangle {
        id: rectangle
        x: 390
        y: 70
        width: 100
        height: 100
        color: "#000000"
    }

    Label {
        id: lblackcard
        x: 400
        y: 80
        width: 80
        height: 80
        color: "#ffffff"
        text: qsTr("Label")
        horizontalAlignment: Text.AlignHCenter
        verticalAlignment: Text.AlignVCenter
    }


}


