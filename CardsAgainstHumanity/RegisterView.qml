import QtQuick 2.0
import QtQuick.Controls 6.2

Item {
    width: 360
    height: 220

    Label {
        id: label
        x: 40
        y: 40
        width: 80
        height: 20
        text: qsTr("Username:")
    }

    Label {
        id: label1
        x: 40
        y: 80
        width: 80
        height: 20
        text: qsTr("Email address:")
    }

    Label {
        id: label2
        x: 40
        y: 120
        width: 80
        height: 20
        text: qsTr("Password:")
    }

    TextField {
        id: tusername
        x: 120
        y: 40
        width: 180
        placeholderText: qsTr("Username")
    }

    TextField {
        id: temail
        x: 120
        y: 80
        width: 180
        placeholderText: qsTr("Email")
    }

    TextField {
        id: tpassword
        x: 120
        y: 120
        width: 180
        echoMode: TextInput.Password
        placeholderText: qsTr("Password")
    }

    Button {
        id: bregister
        x: 40
        y: 160
        width: 100
        text: qsTr("Register")
    }

    Button {
        id: blogin
        x: 200
        y: 160
        width: 100
        text: qsTr("Log in instead")
    }

}
