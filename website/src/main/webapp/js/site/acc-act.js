/**
 * Created by Robotic Seattle on 6/27/2016.
 *
 * Controlling the right-top corner images and populating the browser history with accordion events
 */

function displayRightTopPicture(event, ui) {
    var accordionSectionElementId = ui.newPanel[0].id;
    var dashIndex = accordionSectionElementId.indexOf('-');
    if (dashIndex > 0) {
        var tabElementId = accordionSectionElementId.substr(0, dashIndex);
        var imageElementId = tabElementId + "-img";
        var ext='.png';
        if($('#'+accordionSectionElementId).hasClass('gif-ext')) ext='.gif';
        else if($('#'+accordionSectionElementId).hasClass('jpg-ext')) ext='.jpg';
        $('#' + imageElementId).attr('src', "images/small/" + accordionSectionElementId + ext);
    }
}

function saveAccordionSection2History(event, ui) {
    var accordionSectionElementId = ui.newPanel[0].id;
    if(window.location.hash == "#"+accordionSectionElementId) return; // Prevent saving the same element twice
    var dashIndex = accordionSectionElementId.indexOf('-');
    if (dashIndex > 0) {
        var accordionElementId = accordionSectionElementId.substr(0, dashIndex)+'-acc';
        var accordionElement = $("#"+accordionElementId);
        var activeAccordionSectionIndex = accordionElement.accordion("option", "active");
        var activeTabIndex = $('#tabs').tabs("option", "active");
        var histState = {};
        histState.tabIndex = activeTabIndex;
        histState.accElementId = accordionElementId;
        histState.accSectionIndex = activeAccordionSectionIndex;
        window.history.pushState(histState, JSON.stringify(histState), "main.html#" + accordionSectionElementId);
    }
}

$(function() {
    $(".right-top-picture").on('accordionactivate', displayRightTopPicture);
    $(".accordion").on('accordionactivate', saveAccordionSection2History);
});

