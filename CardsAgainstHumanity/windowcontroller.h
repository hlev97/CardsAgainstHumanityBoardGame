#ifndef WINDOWCONTROLLER_H
#define WINDOWCONTROLLER_H

#include <QObject>

class WindowController : public QObject
{
    Q_OBJECT

public:
    WindowController(QObject *parent = nullptr);
    ~WindowController();

private:

private slots:
    void ShowLoginView();
    void ShowRegisterView();
    void ShowMainMenuView();
    void ShowGameView();

};
#endif // WINDOWCONTROLLER_H
