#ifndef REGISTERVIEW_H
#define REGISTERVIEW_H

#include <QWidget>

namespace Ui {
class RegisterView;
}

class RegisterView : public QWidget
{
    Q_OBJECT

public:
    explicit RegisterView(QWidget *parent = nullptr);
    ~RegisterView();

signals:
    void ShowLoginView();
    void Register(QString email, QString username, QString password);

private:
    Ui::RegisterView *ui;

public slots:
    void SuccesfulRegistration();
    void FailedRegistration(QString reason);
private slots:
    void RegButtonPressed(bool checked);
    void BackToLoginPressed(bool checked);
};

#endif // REGISTERVIEW_H
