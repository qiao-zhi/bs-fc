/**
 * PublicFunctions.js 一些公共的函数
 */

/**
 * 一个值如果是null或者''返回-
 * 
 * @param value
 *            需要处理的值
 * @param length
 *            需要截取的字符的长度的值,未指定的时候返回全部
 * @returns {*} 处理过的值
 */
function replaceNull(value, length) {
	// 判断截取的值是否为空
	if (value == null || value == undefined || value == ""
			|| value == 'undefined') {
		return "-";
	}
	// 判断长度是否为空
	if (length == null || length == '') {
		return value;
	}
	return value.toString().substr(0, length);
}

var NumberUtils = {
	toFixedDecimal : function(value, scale) {
		var defaultValue = 0.0;

		if (!value || isNaN(parseFloat(value))) {
			value = defaultValue;
		}

		if (!scale) {
			scale = 0;
		}

		value = parseFloat(value);
		return value.toFixed(scale);
	},

	toFixedDecimalWithPercent : function(value, scale) {
		value = NumberUtils.toFixedDecimal(value, 10);

		if (!scale) {
			scale = 0;
		}

		value = parseFloat(value * 100);
		return value.toFixed(scale) + "%";
	}
};

function serializeCheckboxValuesByName(name, spliter) {
	if (!spliter) {
		spliter = ",";
	}
	
	return getSerializeCheckboxValues("[name='"+name+"']:checked", spliter);
}

function serializeCheckboxValuesByClass(clazz, spliter) {
	if (!spliter) {
		spliter = ",";
	}
	
	return getSerializeCheckboxValues("." + clazz + ":checked", spliter);
}

function getSerializeCheckboxValues(selector, spliter) {
	var values = [];
	$(selector).each(function() {
		values.push($(this).val());
	});
	
	if (values.length > 0) {
		return values.join(spliter);
	}
	
	return "";
}

/**
 * 关闭当前页的函数
 */
function closeNowPage() {
	var closeTable = $(".layui-tab-title", parent.document).children("li");
	closeTable.each(function() {
		if ($(this).attr("class") == "layui-this") {
			$(this).children("i").trigger("click");// 触发其点击事件(关闭当前tab)--trigger用于触发一个元素的指定事件
		}
	})
}

function exportExcel(formId, url) {
	try {
		var queryForm = $("#" + formId);
		var exportForm = $("<form action='" + url + "' method='post'></form>")

		queryForm.find("input").each(
				function() {
					var name = $(this).attr("name");
					var value = $(this).val();
					exportForm.append("<input type='hidden' name='" + name
							+ "' value='" + value + "'/>")
				});

		queryForm.find("select").each(
				function() {
					var name = $(this).attr("name");
					var value = $(this).val();
					exportForm.append("<input type='hidden' name='" + name
							+ "' value='" + value + "'/>")
				});

		$(document.body).append(exportForm);
		exportForm.submit();
	} catch (e) {
		console.log(e);
	} finally {
		exportForm.remove();
	}
}
