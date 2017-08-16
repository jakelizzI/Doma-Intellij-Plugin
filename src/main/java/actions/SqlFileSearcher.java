package actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ResourceFileUtil;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class SqlFileSearcher {

    private AnActionEvent actionEvent;
    private PsiFile psiFile;

    /**
     * �R���X�g���N�^��ŃC���X�^���X�ϐ��Ƃ���ActionEvent���擾����B
     *
     * @param actionEvent
     */
    SqlFileSearcher(AnActionEvent actionEvent) {
        this.actionEvent = actionEvent;
        this.psiFile = actionEvent.getData(CommonDataKeys.PSI_FILE);
    }

    /**
     * SqlFile�̌����B
     *
     * @return �������ʁB�q�b�g���Ȃ���΋��Optional��Ԃ��B
     */
    public Optional<VirtualFile> search() {

        return getPsiMethod()
                // only having annotation of doma
                //.filter(psiMethod -> AnnotationUtil.isAnnotated(psiMethod, getDomaAnnotations()))
                .map(psiMethod -> {

                    // create module and set scope
                    VirtualFile virtualFile = psiFile.getVirtualFile();
                    Module module = ProjectRootManager.getInstance(psiMethod.getProject()).getFileIndex().getModuleForFile(virtualFile);
                    GlobalSearchScope scope = GlobalSearchScope.moduleRuntimeScope(module, false);

                    // sql file path
                    String sqlFilePath = SqlPath.getSqlFileFullPath(psiFile, psiMethod);

                    // search
                    return ResourceFileUtil.findResourceFileInScope(sqlFilePath, psiMethod.getProject(), scope);
                });
    }

    /**
     * PsiMethod�̎擾
     *
     * @return psiMethod�o�Ȃ���΋��Optional��Ԃ��B
     */
    private Optional<PsiMethod> getPsiMethod() {

        // PsiMethod�o�Ȃ���Ή������Ȃ��B
        Object object = actionEvent.getData(CommonDataKeys.PSI_ELEMENT);

        if (object instanceof PsiMethod) {
            return Optional.of((PsiMethod) object);
        }
        return Optional.empty();
    }

    /**
     * Doma�Ŏg�p�����A�m�e�[�V����
     *
     * @return �A�m�e�[�V�����̃��X�g
     */
    private Collection<String> getDomaAnnotations() {

        List<String> domaAnnotations = new ArrayList<String>();

        domaAnnotations.add("Select");
        domaAnnotations.add("Update");
        domaAnnotations.add("Insert");
        domaAnnotations.add("Delete");

        return domaAnnotations;
    }
}
