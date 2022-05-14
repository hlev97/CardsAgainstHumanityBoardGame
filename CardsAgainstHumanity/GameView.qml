import QtQuick 2.0
import QtQuick.Controls 6.2

Item {
    //todo handle end game, make menu and draw

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

        function onGameState(state, round, maxround, users, scores) {
            userlist.clear();
            for(let i = 0; i < users.length; i++){
                userlist.append({name: users[i], score:scores[i]});
            }

            polling.restart();
            if(currentstate === state)
                return;
            lrounds.text = "Rounds: " + round + "/" + maxround;
            switch(state){
            case "TURN_CHOOSING_CARDS":
                votepane.visible = false;
                pickpane.visible = true;
                bSendPicks.enabled = false;
                console.log("asd");
                nc.getCards();
                break;
            case "TURN_VOTING":
                votepane.visible = true;
                pickpane.visible = false;
                bSendVote.enabled = false;
                nc.getPicks();
                break;

            case "TURN_END_GAME":
                showMainMenuView();
                break;
            default:
            }
            currentstate = state
        }

        function onCardsReceived(blackcard, cards, cardids, numpicks){
            bSendPicks.enabled = false;
            lblackcard.text = blackcard;
            lwcards.picknum = numpicks;
            lwcards.cards = cards;
            lwcards.cardids = cardids;
            lwcards.showcards();
            lpicknum.text = "Pick " + numpicks;
        }

        function onPicksReceived(picks, users, picknum){
            bSendVote.enabled = false;
            lvotecards.playerlist = users;
            lvotecards.answerlist = [];
            lvotecardsmodel.clear();

            for(let i = 0; i < users.length; i++) {
                let card = lblackcard.text;
                if(!card.includes("_")){
                    card = card + picks[i*picknum]
                }
                else{
                    for(let j = 0; j < picknum; j++){
                        let picked = picks[i*picknum + j];
                        card = card.replace("_", picked.slice(0,-1));
                    }
                }
                lvotecardsmodel.append({name : card});
                lvotecards.answerlist.push(card);
            }
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
        visible: true

        Label {
            id: label2
            x: 20
            y: 180
            width: 59
            height: 16
            text: qsTr("Your Cards:")
        }

        ScrollView{
            x: 10
            y: 200

            width: 420
            height: 200
            ScrollBar.vertical.policy: ScrollBar.AlwaysOff

            ListView {
                clip: true
                id: lwcards
                width: 420
                height: 200
                orientation: ListView.Horizontal
                flickableDirection: Flickable.HorizontalFlick
                boundsBehavior: Flickable.StopAtBounds
                property var picks: [];
                property var cardids: [];
                property var cards: [];
                property int picknum : 2;
                property var showcards : function (){
                    picks = [];
                    lplayerwhitecards.clear();
                    cards.forEach(card => {
                                      lplayerwhitecards.append({name : card});
                                  });
                }

                delegate: Item {
                    width: 210
                    height: 200
                    Rectangle {
                        x: 5
                        width: 200
                        height: 200
                        color: "#ffffff"

                        Label {
                            x: 10
                            y: 10
                            width: 190
                            height: 150
                            color: "#000000"
                            text: name
                            horizontalAlignment: Text.AlignHCenter
                            verticalAlignment: Text.AlignVCenter
                            wrapMode: Text.WordWrap
                        }

                        CheckBox {
                            x: 100
                            y: 160
                            width: 20
                            height: 20
                            onClicked: {
                                if(checkState === Qt.Checked)
                                    lwcards.picks.push(parseInt(lwcards.cardids[lwcards.cards.indexOf(name)]));
                                else{
                                    lwcards.picks.splice(lwcards.picks.indexOf(lwcards.cardids[lwcards.cards.indexOf(name)]), 1);
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
            x: 340
            y: 410
            width: 80
            text: qsTr("Send")
            onClicked: {
                nc.sendPickedCards(lwcards.picks);
            }
            enabled: false
        }

        Label {
            id: lpicknum
            x: 300
            y: 415
            width: 31
            height: 16
            text: qsTr("Pick 2")
        }

        Rectangle {
            id: rectangle
            x: 130
            y: 20
            width: 210
            height: 160
            color: "#000000"

            Label {
                id: lblackcard
                x: 5
                y: 5
                width: 200
                height: 150
                color: "#ffffff"
                text: qsTr("Label")
                wrapMode: Text.WordWrap
                horizontalAlignment: Text.AlignHCenter
                verticalAlignment: Text.AlignVCenter
            }
        }

        Label {
            id: label1
            x: 20
            y: 25
            text: qsTr("Black Card:")
        }
    }

    Pane {
        id: userlistpane
        x: 0
        y: 0
        width: 180
        height: 480

        Text {
            y: -5
            x: 5
            text: qsTr("Users:")
            font.bold: true
        }

        ScrollView {
            id: scrollView1
            x: 0
            y: 20
            width: 160
            height: 440
            ScrollBar.horizontal.policy: ScrollBar.AlwaysOff

            ListView {
                clip:true
                id: listView
                width: 160
                height: 440
                delegate: Item {
                    x: 5
                    width: 160
                    height: 30
                    Row {
                        id: row1
                        spacing: 10
                        Text {
                            text: name
                            anchors.verticalCenter: parent.verticalCenter
                            font.bold: true
                        }
                        Text {
                            text: score
                        }
                    }
                }
                model: ListModel {
                    id: userlist
                    ListElement {
                        name: "Grey"
                        score: 3
                    }

                    ListElement {
                        name: "Red"
                        score: 6
                    }

                    ListElement {
                        name: "Blue"
                        score: 8
                    }

                    ListElement {
                        name: "Green"
                        score: 10
                    }
                }
            }
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
            x: 10
            y: 50

            width: 420
            height: 360
            ScrollBar.vertical.policy: ScrollBar.AlwaysOff

            ListView {
                clip:true
                orientation: ListView.Horizontal
                flickableDirection: Flickable.HorizontalFlick
                boundsBehavior: Flickable.StopAtBounds
                id: lvotecards
                width: 420
                height: 360
                visible: true
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
                    width: 210
                    height: 350
                    Rectangle {
                        x: 5
                        width: 200
                        height: 350
                        color: "#ffffff"

                        Label {
                            x: 10
                            y: 10
                            width: 180
                            height: 300
                            color: "#000000"
                            text: name
                            horizontalAlignment: Text.AlignHCenter
                            verticalAlignment: Text.AlignVCenter
                            wrapMode: Text.WordWrap
                        }

                        CheckBox {
                            x: 100
                            y: 320
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
            x: 20
            y: 25
            width: 140
            text: qsTr("Vote for the funniest card:")
        }

        Button {
            enabled: false
            id: bSendVote
            x: 350
            y: 410
            width: 80
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

    /*
    ChartView {
        title: "Bar series"
        anchors.fill: parent
        legend.alignment: Qt.AlignBottom
        antialiasing: true

        BarSeries {
            id: mySeries
            axisX: BarCategoryAxis { categories: ["2007", "2008", "2009", "2010", "2011", "2012" ] }
            BarSet { label: "Bob"; values: [2, 2, 3, 4, 5, 6] }
            BarSet { label: "Susan"; values: [5, 1, 2, 4, 1, 7] }
            BarSet { label: "James"; values: [3, 5, 8, 13, 5, 8] }
        }
    }*/


}
