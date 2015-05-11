function validateQuantity(target) {
	var id = target.id;
	var quantity = target.value;
	var $this = $(target);
	var $next = $this.next();
	if(!isPositiveInteger(quantity)) {
		if($next.length === 0) {
			$this.after($("<span></span>").text("please input positive integer").attr("class", "error"));
		} 
	} else {
		if($next.length > 0 && $next.attr("class") === "error") {
			$next.remove();
		}
	}
}

function isPositiveInteger(s)
{
    var i = +s; // convert to a number
    if (i <= 0) return false; // make sure it's positive
    if (i != ~~i) return false; // make sure there's no decimal part
    return true;
}

function onProcessAllCheckboxClicked(target) {
	var $this = $(target);
	var numberInputSpinnerId = $this.parent().next().find('span').attr('id');
	var spinner = RichFaces.$(numberInputSpinnerId);
	if(target.checked) {
		disableSpinner(spinner);
	} else {
		enableSpinner(spinner);
	}
}

function disableSpinner(component) {
    component.input.attr('disabled', true);
    component.input.attr('value', 0);
    component.originalIncrease = component.increase;
    component.increase = function() {}
    component.originalDecrease = component.decrease;
    component.decrease = function() {}
}

function enableSpinner(component) {
	component.input.attr('disabled', false);
	if (component.originalIncrease) {
	    component.increase = component.originalIncrease;
	    component.originalIncrease = null;
	}
	if (component.originalDecrease) {
	    component.decrease = component.originalDecrease;
	    component.originalDecrease = null;
	}
}