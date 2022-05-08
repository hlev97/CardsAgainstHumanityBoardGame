#include "windowcontroller.h"
/*#include "./ui_windowcontroller.h"
#include "loginview.h"
#include "registerview.h"
#include "menuview.h"*/

WindowController::WindowController(QObject *parent) : QObject(parent)
{
    ShowLoginView();
}

void WindowController::ShowLoginView(){
}

void WindowController::ShowRegisterView(){
}

void WindowController::ShowMainMenuView(){
}

void WindowController::ShowGameView(){

}

WindowController::~WindowController()
{

}

