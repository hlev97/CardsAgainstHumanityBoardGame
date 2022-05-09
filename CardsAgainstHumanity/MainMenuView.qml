import QtQuick 2.0
import QtQuick.Controls 6.2

Item {

    width: 640
    height: 480

    signal showLoginView()
    signal showRegisterView()
    signal showMainMenuView()
    signal showGameView()

    Connections{
        target: nc

        function onRoomListReceived(list) {
            lroomsmodel.clear();
            list.forEach(item => lroomsmodel.append({name: item}));
        }
    }

    Pane {
        id: pane
        x: 0
        y: 0
        width: 320
        height: 480

        ListView {
            id: lrooms
            x: 20
            y: 100
            width: 270
            height: 280
            model: ListModel {
                id: lroomsmodel
            }
            delegate: Item {
                x: 5
                width: 80
                height: 40
                Row {
                    id: row1
                    spacing: 10

                    Text {
                        text: name
                        anchors.verticalCenter: parent.verticalCenter
                        font.bold: true
                    }

                    Button {
                        text : qsTr("Join room")
                        onClicked: nc.joinRoom(name);
                    }
                }
            }
        }

        TextField {
            id: tfsearch
            x: 20
            y: 20
            width: 150
            height: 24
            placeholderText: qsTr("Room name")
        }

        TextField {
            id: tfcreate
            x: 90
            y: 400
            height: 24
            placeholderText: qsTr("Room name")
        }

        Button {
            id: bsearch
            x: 180
            y: 20
            width: 50
            text: qsTr("Search")
        }

        Button {
            id: brefresh
            x: 240
            y: 20
            width: 50
            text: qsTr("Refresh")
            onClicked: nc.getRoomList();
        }

        Button {
            id: bcreate
            x: 220
            y: 400
            text: qsTr("Create Room")
            onClicked: nc.createRoom(tfcreate.text);
        }

        Label {
            id: label
            x: 20
            y: 60
            width: 40
            height: 24
            text: qsTr("Rooms:")
        }

        Label {
            id: label1
            x: 20
            y: 400
            width: 70
            height: 24
            text: qsTr("Room name:")
        }
    }

    Pane {
        id: pane1
        x: 320
        y: 0
        width: 320
        height: 480

        ListView {
            id: listView1
            x: 20
            y: 100
            width: 270
            height: 280
            model: ListModel {
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
                x: 5
                width: 80
                height: 40
                Row {
                    id: row2
                    spacing: 10
                    Rectangle {
                        width: 40
                        height: 40
                        color: colorCode
                    }

                    Text {
                        text: name
                        anchors.verticalCenter: parent.verticalCenter
                        font.bold: true
                    }
                }
            }
        }

        TextField {
            id: tfinvite
            x: 80
            y: 440
            width: 130
            placeholderText: qsTr("Username")
        }

        Button {
            id: binvite
            x: 220
            y: 440
            width: 70
            text: qsTr("Invite User")
            onClicked: {
                nc.invitePlayer(tfinvite.text);
            }
        }

        Button {
            id: bleave
            x: 210
            y: 20
            width: 80
            text: qsTr("Leave Room")
            onClicked: {
                nc.leaveRoom();
            }
        }

        Button {
            id: bplus
            x: 80
            y: 400
            width: 24
            text: qsTr("+")
            onClicked: {
                lrounds.text = parseInt(lrounds.text) + 1;
            }
        }

        Button {
            id: bminus
            x: 150
            y: 400
            width: 24
            text: qsTr("-")
            onClicked: {
                lrounds.text = lrounds.text - 1;
                if(lrounds.text < 1)
                    lrounds.text = 1;
            }
        }

        Label {
            id: label2
            x: 20
            y: 440
            width: 60
            text: qsTr("Username:")
        }

        Label {
            id: label3
            x: 20
            y: 60
            width: 40
            height: 24
            text: qsTr("Users:")
        }

        Label {
            id: lcurrentroom
            x: 20
            y: 20
            width: 120
            height: 24
            text: qsTr("currentRoom")
        }

        Label {
            id: lrounds
            x: 120
            y: 400
            width: 24
            height: 24
            text: qsTr("5")
        }

        Label {
            id: label6
            x: 20
            y: 400
            width: 50
            height: 24
            text: qsTr("Rounds:")
        }

        Button {
            id: bstartgame
            x: 220
            y: 400
            width: 70
            text: qsTr("Start Game")
            onClicked: nc.startGame(parseInt(lrounds.text));
        }
    }
}

/*##^##
Designer {
    D{i:0;autoSize:true;height:480;width:640}
}
##^##*/
