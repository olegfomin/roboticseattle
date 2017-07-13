/*
 * Created by Robotic Seattle on 6/28/2016.
 */


/**
 * Parses the accordion section or tab id (it may or may not be prepended with #) and makes the target JSON with indexes
 * @param elementId
 * @returns {parsed, tabIndex, accElementId, accSectionIndex} - tab index, parental accordion element id without hash, accordion section index
 */
function id2Target(elementId) {
    var navTarget = {};

    var elementIdNoHash = (elementId.indexOf("#")==0) ? elementId.substr(1) : elementId;

    var tabElemId = elementIdNoHash; // At first assuming there is not dashes after # so it is a tab id only and stripping the # off
    var accordionElemId = tabElemId+'-acc';
    var accordionSectionIndex = 0;

    var dashIndex = elementIdNoHash.indexOf("-");
    if(dashIndex > 0) { // Well, assumption was wrong as there were dashes
        tabElemId = elementIdNoHash.substr(0, dashIndex);
        accordionElemId = tabElemId+'-acc';
        var accordionSectionElem = $("#"+elementIdNoHash);
        if(accordionSectionElem.length > 0) {
            accordionSectionIndex = $("#"+accordionElemId + " > div").index(accordionSectionElem[0]);
        }
    }

    var tabElem = $("#"+tabElemId);
    if(tabElem.length > 0) {
        var tabIndex = ($('#tabs > div').index(tabElem[0]));
        if(tabIndex > -1) {
            navTarget.tabIndex = tabIndex;
            navTarget.accElementId = accordionElemId;
            navTarget.accSectionIndex = accordionSectionIndex;
        }
    }

    return navTarget;
}

function activateSection(elementId) {
    var navTarget = id2Target(elementId);
    $("#tabs").tabs("option", "active", navTarget.tabIndex);
    $("#" + navTarget.accElementId).accordion("option", "active", navTarget.accSectionIndex);
    return false;
}

/**
 * Very initial navigation to an element if its id was provided after #
 */
$(function() {
    if (location.hash.length > 1) {
        activateSection(location.hash);
    }
})

/*
  Navigation through the history
 */
window.onpopstate = function(event) {
    if(event.state != null) {
        $("#tabs").tabs("option", "active", event.state.tabIndex);
        if(event.state.accElementId != null) { // Might have been previously set by acc-act.js
            $('#'+event.state.accElementId).accordion("option", "active", event.state.accSectionIndex);
        }
    } else {
        // Just moving to the first tab and first accordion section
        $("#tabs").tabs("option", "active", 0);
        $("#main-acc").accordion("option", "active", 0);
    }
};


