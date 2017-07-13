/**
 * Created by Robotic Seattle on 6/27/2016.
 */

var lastHistoryState = {'tabIndex' : 0, 'accElementId' : 'main-acc', 'accSectionIndex' : 0};
var lastHashRef = 'main-acc-intro';


function saveTab2History(event, ui) {
    if($(ui.newPanel[0]).hasClass('dispensable')) { // Not memorizing 'More ...' tab because it is dispensable
        window.history.pushState(lastHistoryState, JSON.stringify(lastHistoryState), "main.html#" + lastHashRef);   // Sticking the previous one in instead
        return;
    }
    var activeTabIndex = $('#tabs').tabs("option", "active");
    var histState = {};
    histState.tabIndex = activeTabIndex;
    var tabElementId = ui.newPanel[0].id;
    if(window.location.hash == "#"+tabElementId) return; // Prevent saving the same element twice
    var hashRef = tabElementId;
    var accordionElementId = tabElementId+"-acc";
    var accordionElement = $('#'+accordionElementId);
    if(accordionElement.length > 0) { // The accordion found
        var accordionSectionElement = $('#'+accordionElementId+" .ui-accordion-content-active");
        if(window.location.hash == "#"+accordionSectionElement.attr('id')) return; // Prevent saving the same element twice
        var activeAccordionSectionIndex =  accordionElement.accordion("option", "active");
        hashRef = accordionSectionElement.attr('id');
        histState.accElementId=accordionElementId;
        histState.accSectionIndex = activeAccordionSectionIndex;
    }

    window.history.pushState(histState, JSON.stringify(histState), "main.html#" + hashRef);
    lastHistoryState = histState;
    lastHashRef = hashRef;
}

function removeDispensableTabIfOpen(event, ui) {
    if(!$(ui.newPanel[0]).hasClass('dispensable')) { // Anything closes the 'More ...' tag
        var indexArray = $("#tabs").tabs( "option", "disabled");
        if($.inArray(6, indexArray) != 0) {
            revomeMoreTab();
        }
    }
}

$(function() {
    $("#tabs").on('tabsactivate', saveTab2History);
    $("#tabs").on('tabsactivate', removeDispensableTabIfOpen);
});
