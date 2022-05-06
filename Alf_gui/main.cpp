#include "windowcontroller.h"

#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    WindowController w;
    w.show();
    return a.exec();
}
