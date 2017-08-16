package actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.Optional;

public class GoToSqlFileOrCreateFile extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        // search classのインスタンス化
        SqlFileSearcher sqlFileSearcher = new SqlFileSearcher(e);
        
        // search
        Optional<VirtualFile> virtualFile = sqlFileSearcher.search();

        // virualFileが存在していればSqlFileをOpen
        virtualFile.ifPresent(sqlFile -> FileEditorManager.getInstance(e.getProject()).openFile(sqlFile, true));

        Notifications.Bus.notify(new Notification(
                "notify","title","sqlFile : " + virtualFile, NotificationType.INFORMATION
        ));

        // file open
//        FileEditorManager.getInstance(method.getProject()).openFile(sqlFile, true);
//    
//        SqlFileCreateDialog dialog = new SqlFileCreateDialog(e.getProject(), true, "create sql");
//        dialog.show();

    }
}
