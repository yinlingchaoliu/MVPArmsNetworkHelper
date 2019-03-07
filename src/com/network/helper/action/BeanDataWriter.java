package com.network.helper.action;

import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.network.helper.common.PsiFileUtils;
import com.network.helper.config.ArmConstant;
import com.network.helper.entity.NetWorkEntity;
import com.network.helper.ui.ToastUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 *  Api插入文件
 */
public class BeanDataWriter extends WriteCommandAction {
    private Project project;
    private PsiFile psiFile;
    private PsiClass psiClass;
    private NetWorkEntity entity;
    private PsiDirectory directory;
    private boolean isGenerate = false; //bean文件是否需要生成

    public BeanDataWriter(@NotNull PsiFile psiFile, NetWorkEntity entity){
        super(psiFile.getProject(),psiFile);

        PsiFile findPsiFile = PsiFileUtils.getFileByName(psiFile, "BaseDataBean.java");
        this.directory = findPsiFile.getParent();
        this.project = psiFile.getProject();
        this.psiFile = psiFile;
        this.entity = entity;
        init();
    }

    private BeanDataWriter(@Nullable Project project, PsiFile... files) {
        super(project, files);
    }

    @Override
    protected void run(@NotNull Result result) throws Throwable {
        write();
    }

    private void write(){
        if (!isGenerate) return;
        directory.createFile(entity.getClearResponseBean()+".java");
        PsiFile file = directory.findFile(entity.getClearResponseBean()+".java");
        VirtualFile vf= file.getVirtualFile();
        String  content = ArmConstant.getBeanStr(entity);
        ToastUtil.show(content);
        try {
            vf.setBinaryContent(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void init(){
        if (entity.getClearResponseBean().equals("")){
            isGenerate = false;
            return;
        }

        PsiFile psiFile = directory.findFile(entity.getClearResponseBean()+".java");
        if (psiFile == null){
            isGenerate = true;
        }

    }

    //常用工具
    private boolean isMethodExist(NetWorkEntity entity){
        PsiMethod[] methods = psiClass.findMethodsByName(ArmConstant.getInterfaceMethodName(entity),false);
        PsiMethod findViewsMethod = methods.length > 0 ? methods[0] : null;
        return findViewsMethod != null;
    }

}
