#include "loginview.h"
#include "ui_loginview.h"

LoginView::LoginView(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::LoginView)
{
    ui->setupUi(this);
    connect(ui->loginbutton, &QPushButton::clicked, this, &LoginView::LoginButtonPressed);
    connect(ui->registerbutton, &QPushButton::clicked, this, &LoginView::RegButtonPressed);
}

void LoginView::LoginButtonPressed(bool checked){
    emit LoginPressed(ui->username->toPlainText(), ui->password->toPlainText());
}

void LoginView::RegButtonPressed(bool checked){
    emit ShowRegisterView();
}

void LoginView::SuccessfulLogin(){
    emit LoginView::ShowMenuView();
}

LoginView::~LoginView()
{
    delete ui;
}
