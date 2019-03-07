package com.network.helper.ui;

import com.intellij.openapi.project.Project;
import com.network.helper.config.ArmConstant;
import com.network.helper.entity.NetWorkEntity;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ArmNetworkForm extends JFrame {

    private JPanel contentPane;
    private JTextField packageNameTextField;
    private JTextField urlTextField;
    private JTextField methodTextField;
    private JTextField requestInterfaceTextField;
    private JTextField responseBeanTextField;
    private JTextField bizModuleTextField;
    private JButton cancelButton;
    private JButton okButton;
    private CallBack callBack;

    public ArmNetworkForm(String selectText, Project project){

        setContentPane(contentPane);
        setTitle("ArmMvp网络工具自动生成类");
        setAlwaysOnTop(true);
        getRootPane().setDefaultButton(okButton);
        //ok 按钮设置监听
        okButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isAllInput()){
                    show(project,"请把编辑框内容输入全");
                    return;
                }
                NetWorkEntity entity = new NetWorkEntity();
                entity.setPsiInput(selectText);
                entity.setPackageName(packageNameTextField.getText());
                entity.setUrl(urlTextField.getText());
                entity.setMethod(methodTextField.getText());
                if (!ArmConstant.checkInterface(requestInterfaceTextField.getText())){
                    show(project,"接口范例:activePay()");
                    return;
                }
                entity.setRequestInterface(ArmConstant.trimEnd(requestInterfaceTextField.getText()));
                entity.setResponseBean(responseBeanTextField.getText());
                entity.setBizModule(bizModuleTextField.getText());
                //插入代码段
                if (callBack!=null){
                    callBack.writeAction(entity);
                    dispose();
                }
            }
        });

        cancelButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        initView(selectText);
    }

    private void initView(String selectText){
        packageNameTextField.setText("com.hexin.xffq");
        String url = ArmConstant.parseUrl(selectText);
        urlTextField.setText(url);
        String requestInterface = ArmConstant.parseInterface(selectText);
        requestInterfaceTextField.setText(requestInterface);
        methodTextField.setText("post");
        responseBeanTextField.setText("Object");
        bizModuleTextField.setText("");
    }

    private boolean isAllInput(){
        if (isEmpty(packageNameTextField.getText()) ||
                isEmpty(urlTextField.getText()) ||
                isEmpty(methodTextField.getText()) ||
                isEmpty(requestInterfaceTextField.getText()) ||
                isEmpty(responseBeanTextField.getText()) ||
                isEmpty(bizModuleTextField.getText())){
            return false;
        }
        return true;
    }

    private boolean isEmpty(String str){
        if (str == null || str.trim().length() == 0 || str.trim().trim().equals(";")){
            return true;
        }
        return false;
    }

    private static void show(Project project, String msg){
        if (project == null){
            System.out.println(msg);
        }else {
            ToastUtil.show(project,msg);

        }
    }

    public interface CallBack{
        void writeAction(NetWorkEntity entity);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public static void main(String[] args) {
        ArmNetworkForm dialog = new ArmNetworkForm("    public static final String DEPOSITORY_ACTIVEPAY_INTERFACENAME = \"/petty/borrow/activePay\";\n",null);
        dialog.setCallBack(new CallBack() {
            @Override
            public void writeAction(NetWorkEntity entity) {

            }
        });
        dialog.pack();
        dialog.setVisible(true);
    }

}
