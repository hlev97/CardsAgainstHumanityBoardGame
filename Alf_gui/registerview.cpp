#include "registerview.h"
#include "ui_registerview.h"

RegisterView::RegisterView(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::RegisterView)
{
    ui->setupUi(this);
    ui->password->setEchoMode(QLineEdit::Password);
    connect(ui->register_2, &QPushButton::clicked, this, &RegisterView::RegButtonPressed);
    connect(ui->login, &QPushButton::clicked, this, &RegisterView::BackToLoginPressed);
}

void RegisterView::BackToLoginPressed(bool checked){
    emit ShowLoginView();
}

void RegisterView::RegButtonPressed(bool checked){
    emit Register(ui->email->text(), ui->username->text(), ui->password->text());
}

void RegisterView::SuccesfulRegistration(){
    emit ShowLoginView();
}

void RegisterView::FailedRegistration(QString msg){
    ui->error->setText(msg);
}

RegisterView::~RegisterView()
{
    delete ui;
}
