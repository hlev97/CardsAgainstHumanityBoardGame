import QtQuick 2.15
import QtQuick.Window 2.15
import QtQuick.Controls 6.2
import QtQuick.Layouts 6.0
import QtQuick.Controls.Windows 6.0

import cah.networkcontroller 1.0

Window {
    width: 640
    height: 320
    visible: true
    title: qsTr("Cards Against Humanity")

    NetworkController{
        id: nc
    }


    LoginView {
        id: loginView
        x: 0
        y: 0

        RegisterView {
            id: registerView
            x: 282
            y: 98
        }

    }


}
