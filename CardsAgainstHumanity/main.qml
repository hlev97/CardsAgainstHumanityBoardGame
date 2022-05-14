import QtQuick 2.15
import QtQuick.Window 2.15
import QtQuick.Controls 6.2
import QtQuick.Layouts 6.0
import QtQuick.Dialogs

//Window responsible for loading different views of the app and also handling menus
ApplicationWindow {
    width: 640
    height: 320
    visible: true
    title: qsTr("Cards Against Humanity")
    id: window

    //Shows login view
    function showLoginView(){
        displayedview.source = "qrc:/LoginView.qml"
    }

    //Shows register voiew
    function showRegisterView(){
        displayedview.source = "qrc:/RegisterView.qml"
    }

    //Shows main menu view
    function showMainMenuView(){
        displayedview.source = "qrc:/MainMenuView.qml"
    }

    //Shows game view
    function showGameView(){
        displayedview.source = "qrc:/GameView.qml"
    }

    Loader {
        id: displayedview
        onLoaded: {
            window.maximumHeight = height;
            window.maximumWidth = width;
            window.minimumHeight = height;
            window.minimumWidth = width;
            window.height = height;
            window.width = width;
        }
        source: "qrc:/LoginView.qml"
    }

    //Handles for different view's signals to load the corresponding view
    Connections {
        target: displayedview.item

        function onShowLoginView(){
            showLoginView();
        }
        function onShowRegisterView(){
            showRegisterView();
        }
        function onShowMainMenuView(){
            showMainMenuView();
        }
        function onShowGameView(){
            showGameView();
        }
    }

    menuBar: MenuBar{
        Menu{
            title: qsTr("File")
            Action{
                text: qsTr("About")
                onTriggered: aboutdialog.open()
            }
            Action{
                text: qsTr("Close")
                onTriggered: window.close();
            }
        }
    }

    Dialog{
        title: qsTr("Cards Against Humanity")
        width: 320
        height: 120

        Label{
            anchors.fill: parent
            horizontalAlignment: Text.AlignHCenter
            verticalAlignment: Text.AlignVCenter
            text: qsTr("Cards Against Humanity is a fill-in-the-blank party game that turns your awkward personality and lackluster social skills into hours of fun! Wow.")
            wrapMode: Text.WordWrap
        }

        Button{
            width: 40
            x: 270
            y: 60
            text: qsTr("OK")
            onClicked: aboutdialog.close();
        }

        id: aboutdialog
    }

}
