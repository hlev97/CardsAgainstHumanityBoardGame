import QtQuick 2.15
import QtQuick.Window 2.15
import QtQuick.Controls 6.2
import QtQuick.Layouts 6.0
import QtQuick.Controls.Windows 6.0

Item {
    width: 320
    height: 180

    signal showLoginView()
    signal showRegisterView()
    signal showMainMenuView()
    signal showGameView()

    Connections{
        target: nc

        function onSuccessfullyLoggedIn(){
            showMainMenuView();
        }

    }

    Button {
        id: breg
        x: 40
        y: 120
        width: 100
        height: 24
        text: qsTr("Register")

        onClicked: showRegisterView();
    }

    Button {
        id: blogin
        x: 200
        y: 120
        width: 100
        height: 24
        text: qsTr("Login")

        onClicked: nc.login(tusername.text, tpassword.text);
    }

    TextField {
        id: tusername
        x: 120
        y: 40
        width: 180
        height: 20
        placeholderText: qsTr("Username")
    }

    TextField {
        id: tpassword
        x: 120
        y: 80
        width: 180
        height: 20
        echoMode: TextInput.Password
        placeholderText: qsTr("Password")
    }

    Label {
        x: 40
        y: 40
        width: 70
        height: 20
        text: qsTr("Username:")
    }

    Label {
        id: label1
        x: 40
        y: 80
        width: 70
        height: 20
        text: qsTr("Password:")
    }

}
