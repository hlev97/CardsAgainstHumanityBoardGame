#ifndef LOGINVIEW_H
#define LOGINVIEW_H

#include <QWidget>

namespace Ui {
class LoginView;
}

class LoginView : public QWidget
{
    Q_OBJECT

public:
    explicit LoginView(QWidget *parent = nullptr);
    ~LoginView();

signals:
    void LoginPressed(QString username, QString password);
    void ShowRegisterView();
    void ShowMenuView();

private:
    Ui::LoginView *ui;

private slots:
    void SuccessfulLogin();
    //void FailedLogin();
    void LoginButtonPressed(bool checked);
    void RegButtonPressed(bool checked);
};

#endif // LOGINVIEW_H
