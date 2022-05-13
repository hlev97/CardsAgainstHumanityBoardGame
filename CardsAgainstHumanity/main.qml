import QtQuick 2.15
import QtQuick.Window 2.15
import QtQuick.Controls 6.2
import QtQuick.Layouts 6.0
import QtQuick.Controls.Windows 6.0

ApplicationWindow {
    width: 640
    height: 320
    visible: true
    title: qsTr("Cards Against Humanity")
    id: window

    function showLoginView(){
        displayedview.source = "qrc:/LoginView.qml"
    }

    function showRegisterView(){
        displayedview.source = "qrc:/RegisterView.qml"
    }

    function showMainMenuView(){
        displayedview.source = "qrc:/MainMenuView.qml"
    }

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
        source: "qrc:/MainMenuView.qml"
    }

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
            }
            Action{
                text: qsTr("Close")
                onTriggered: window.close();
            }
        }
    }

}
