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
     * コンストラクタ上でインスタンス変数としてActionEventを取得する。
     *
     * @param actionEvent
     */
    SqlFileSearcher(AnActionEvent actionEvent) {
        this.actionEvent = actionEvent;
        this.psiFile = actionEvent.getData(CommonDataKeys.PSI_FILE);
    }

    /**
     * SqlFileの検索。
     *
     * @return 検索結果。ヒットしなければ空のOptionalを返す。
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
     * PsiMethodの取得
     *
     * @return psiMethod出なければ空のOptionalを返す。
     */
    private Optional<PsiMethod> getPsiMethod() {

        // PsiMethod出なければ何もしない。
        Object object = actionEvent.getData(CommonDataKeys.PSI_ELEMENT);

        if (object instanceof PsiMethod) {
            return Optional.of((PsiMethod) object);
        }
        return Optional.empty();
    }

    /**
     * Domaで使用されるアノテーション
     *
     * @return アノテーションのリスト
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
