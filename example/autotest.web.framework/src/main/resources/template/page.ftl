package ${page.packageName};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.core.ui.Selector;
import org.suren.autotest.web.framework.core.ui.FileUpload;
import org.suren.autotest.web.framework.core.ui.CheckBoxGroup;
import org.suren.autotest.web.framework.page.Page;

/**
 * @author suren
 * ${page.comment}
 */
@Component
public class ${page.name} extends Page {
<#list page.fields as field>
	/**
	 * ${field.comment}
	 */
	@Autowired
	private ${field.type} ${field.name};
</#list>
	/**
	 * getter and setter methods zone
	 */
<#list page.fields as field>
	public ${field.type} ${field.getterMethod}()
	{
		return ${field.name};
	}
	public void ${field.setterMethod}(${field.type} ${field.name})
	{
		this.${field.name} = ${field.name};
	}
</#list>
}
