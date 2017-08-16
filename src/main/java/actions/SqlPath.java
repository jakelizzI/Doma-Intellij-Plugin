package actions;

import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;

public class SqlPath {

    private static String sqlRoot = "META-INF";

    public static String getSqlFileRoot() {
        return sqlRoot;
    }

    /**
     * sqlを作成するディレクトリまでを返却する。
     * 
     * @param psiFile Daoファイル（それ以外の場合はしらん）
     * @return
     */
    public static String getSqlFilePath(PsiFile psiFile) {

        PsiPackage psiPackage = JavaDirectoryService.getInstance().getPackage(psiFile.getContainingDirectory());

        // make file path
        StringBuilder sqlFilePath = new StringBuilder("");
        return sqlFilePath
                .append(sqlRoot)
                .append("/").append(psiPackage.getQualifiedName().replace(".", "/"))
                .append("/").append(psiFile.getName().replace(".java", ""))
                .toString();
    }

    public static String getSqlFileFullPath(PsiFile psiFile, PsiMethod psiMethod) {

        StringBuilder sb = new StringBuilder(getSqlFilePath(psiFile));

        return sb.append("/").append(psiMethod.getName()).append(".sql").toString();
    }
}
