import QtQuick 2.15
import QtQuick.Window 2.15
import QtQuick.Controls 6.2
import QtQuick.Layouts 6.0

//Views responsible for showing the Main Menu screen
Item {

    width: 640
    height: 480

    signal showLoginView()
    signal showRegisterView()
    signal showMainMenuView()
    signal showGameView()
    property bool beenczar: false
    id: root

    //When loading the component we automatically refresh the list of rooms
    Component.onCompleted: nc.getRoomList;

    function leaveroom(){
        //Actions to do on leaving the room: hiding the pane, and disabling controls
        nc.leaveRoom();
        lcurrentroom.text = "";
        polling.stop();
        listplayersmodel.clear();
        lrounds.text = "";

        root.beenczar = false;
        lroundtext.visible = false
        lrounds.visible = false;
        lrounds.text = 5;
        bplus.visible = false;
        bminus.visible = false;
        bstartgame.visible = false;
        roompane.visible = false;
    }

    Connections{
        target: nc

        //On receiving the list of rooms from the network controller, load it into the corresponding view element.
        function onRoomListReceived(list) {
            lroomsmodel.roomlist = list;
            lroomsmodel.showlist();
        }

        //On receiving data for a room we joined to, load it into the corresponding view elements.
        function onRoomDataReceived(players, czarname, rounds, isplayerczar, iskicked) {

            if(iskicked){ //leave the room if the player is kicked
                nc.leaveroom();
                return;
            }

            listplayersmodel.clear();
            players.forEach(item => listplayersmodel.append({name: item, isCzar: item === czarname, isplayerczar: isplayerczar}));

            if(isplayerczar && !root.beenczar){ //Only enable special controls if the player became czar.
                lroundtext.visible = true
                root.beenczar = true;
                lrounds.visible = true;
                lrounds.text = 5;
                bplus.visible = true;
                bminus.visible = true;
                bstartgame.visible = true;
            }
            if(!isplayerczar){ //Hide special controls if the player is not czar.
                lroundtext.visible = false
                root.beenczar = false;
                lrounds.visible = false;
                lrounds.text = 5;
                bplus.visible = false;
                bminus.visible = false;
                bstartgame.visible = false;
            }

            polling.restart(); //We only continue polling after data receive, so congestion can be avoided.
        }

        //On joining to room we start polling for room data.
        function onSuccessfullyJoinedRoom(roomname) {
            nc.getRoomData();
            roompane.visible = true;
            listplayersmodel.clear();
            lcurrentroom.text = roomname;
        }

        //When connecting to a started game, we switch to game view
        function onGameStarted(){
            showGameView();
        }
    }

    //Timer responsible for polling the state of the room the user joined to.
    Timer{
        id: polling
        running: false
        repeat: false
        interval: 1000
        onTriggered: nc.getRoomData();
    }

    //Pane listing the rooms
    Pane {
        id: pane
        x: 0
        y: 0
        width: 320
        height: 480

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
            y: 410
            height: 24
            placeholderText: qsTr("Room name")
        }

        Button {
            id: bsearch
            x: 180
            y: 20
            width: 50
            text: qsTr("Search")
            onClicked: {
                lroomsmodel.searchstring = tfsearch.text;
                lroomsmodel.showlist();
            }
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
            y: 410
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
            y: 410
            width: 70
            height: 24
            text: qsTr("Room name:")
        }

        ScrollView {
            x: 20
            y: 90
            width: 270
            height: 310
            id: scrollView
            ScrollBar.horizontal.policy: ScrollBar.AlwaysOff

            ListView {
                clip: true
                id: lrooms
                x: 0
                y: 0
                width: 270
                height: 280
                model: ListModel {
                    property var roomlist: []
                    property string searchstring: ""
                    property var showlist: function(){
                        clear();
                        roomlist.forEach(room => {
                                             if(room.includes(searchstring))
                                             append({name: room});
                                         });
                    }

                    id: lroomsmodel
                    ListElement {
                        name: "Asd"
                    }
                    ListElement {
                        name: "Grey"
                    }
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
        }
    }

    //Pane showing the state of the room the player joined to.
    Pane {
        id: roompane
        x: 320
        y: 0
        width: 320
        height: 480
        visible:false

        ScrollView {
            x: 20
            y: 90
            width: 270
            height: 280
            ScrollBar.horizontal.policy: ScrollBar.AlwaysOff

            ListView {
                clip: true
                id: listView1
                x: 0
                y: 0
                width: 270
                height: 280
                model: ListModel {
                    id: listplayersmodel
                    ListElement {
                        name: "Grey"
                        isCzar: false
                        isplayerczar: false
                    }
                    ListElement {
                        name: "Grey"
                        isCzar: false
                        isplayerczar: false
                    }
                    ListElement {
                        name: "Grey"
                        isCzar: false
                        isplayerczar: false
                    }
                    ListElement {
                        name: "Grey"
                        isCzar: false
                        isplayerczar: false
                    }
                    ListElement {
                        name: "Grey"
                        isCzar: false
                        isplayerczar: false
                    }
                }
                delegate: Item {
                    x: 5
                    width: 80
                    height: 40
                    Row {
                        id: row2
                        spacing: 10

                        Text {
                            text: name
                            anchors.verticalCenter: parent.verticalCenter
                            font.bold: true

                            Component.onCompleted: {
                                if(isCzar === true)
                                    font.underline = true
                            }
                        }

                        Button {
                            text: qsTr("Kick")
                            onClicked: nc.kickPlayer(name);

                            Component.onCompleted: {
                                if(isplayerczar === true)
                                    visible = true;
                                else
                                    visible = false;
                            }
                        }
                    }
                }
            }
        }

        TextField {
            id: tfinvite
            x: 80
            y: 410
            width: 130
            placeholderText: qsTr("Username")
        }

        Button {
            id: binvite
            x: 220
            y: 410
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
                nc.leaveroom();
            }
        }

        Button {
            visible: false
            id: bplus
            x: 80
            y: 380
            width: 24
            text: qsTr("+")
            onClicked: {
                lrounds.text = parseInt(lrounds.text) + 1;
            }
        }

        Button {
            visible: false
            id: bminus
            x: 150
            y: 380
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
            y: 410
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
            y: 24
            width: 120
            height: 16
            text: qsTr("currentRoom")
        }

        Label {
            visible: false
            id: lrounds
            x: 120
            y: 380
            width: 24
            height: 24
            text: qsTr("5")
        }

        Label {
            id: lroundtext
            visible: false
            x: 20
            y: 380
            width: 50
            height: 24
            text: qsTr("Rounds:")
        }

        Button {
            visible: false
            id: bstartgame
            x: 220
            y: 380
            width: 70
            text: qsTr("Start Game")
            onClicked: nc.startGame(parseInt(lrounds.text));
        }
    }

}


