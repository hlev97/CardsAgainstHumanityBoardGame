#include "windowcontroller.h"
#include "./ui_windowcontroller.h"
#include "loginview.h"
#include "registerview.h"
#include "menuview.h"

WindowController::WindowController(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::WindowController)
{
    ui->setupUi(this);
    ShowLoginView();
}

void WindowController::ShowLoginView(){
    LoginView* currentwidget = new LoginView();
    resize(currentwidget->size());
    setCentralWidget(currentwidget);
    connect(currentwidget,&LoginView::ShowMenuView, this, &WindowController::ShowMainMenuView);
    connect(currentwidget,&LoginView::ShowRegisterView, this, &WindowController::ShowRegisterView);
}

void WindowController::ShowRegisterView(){
    RegisterView* rv = new RegisterView();
    resize(rv->size());
    setCentralWidget(rv);
    connect(rv, &RegisterView::ShowLoginView, this, &WindowController::ShowLoginView);
}

void WindowController::ShowMainMenuView(){
    MenuView* mv = new MenuView();
    resize(mv->size());
    setCentralWidget(mv);
    connect(mv, &MenuView::ShowGameView, this, &WindowController::ShowGameView);
}

void WindowController::ShowGameView(){

}

WindowController::~WindowController()
{
    delete ui;
}

