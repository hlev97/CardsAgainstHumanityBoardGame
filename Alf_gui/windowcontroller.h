#ifndef WINDOWCONTROLLER_H
#define WINDOWCONTROLLER_H

#include <QMainWindow>

QT_BEGIN_NAMESPACE
namespace Ui { class WindowController; }
QT_END_NAMESPACE

class WindowController : public QMainWindow
{
    Q_OBJECT

public:
    WindowController(QWidget *parent = nullptr);
    ~WindowController();

private:
    Ui::WindowController *ui;

private slots:
    void ShowLoginView();
    void ShowRegisterView();
    void ShowMainMenuView();
    void ShowGameView();

};
#endif // WINDOWCONTROLLER_H
