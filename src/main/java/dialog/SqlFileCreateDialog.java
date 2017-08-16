package dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SqlFileCreateDialog extends DialogWrapper {

    public SqlFileCreateDialog(@Nullable Project project, boolean canBeParent, String title) {
        super(project, canBeParent);
        super.init();
        super.setTitle(title);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        JPanel panel = new JPanel();
        JLabel label = new JLabel("SQLファイルを作成しますか？");

        panel.add(label);

        return panel;
    }
}
