import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import mybatis.log.QueryBinder;

import java.io.IOException;

public class QueryBindAction extends AnAction {

	@Override
	public void actionPerformed(AnActionEvent e) {
		Editor editor = e.getData(PlatformDataKeys.EDITOR);
		if (editor != null) {
			String log = editor.getSelectionModel().getSelectedText();
			QueryBinder queryBinder = new QueryBinder(log);
			String bindedQuery = null;
			try {
				bindedQuery = queryBinder.bind();
				Messages.showInfoMessage(bindedQuery, "result");
			} catch (IOException ioException) {
				Messages.showInfoMessage(ioException.getMessage(), "result");
				ioException.printStackTrace();
			}
		}
	}
}
