// ActionScript file
public var selectedNode:XML; 
import flexlib.controls.SuperTabBar;
import flexlib.events.TabReorderEvent;
import mx.controls.Alert;
import mx.containers.VBox;
import mx.containers.Canvas;
import flexlib.controls.tabBarClasses.SuperTab;

[Embed(source="./assets/document.png")]
private var document_icon:Class;

[Embed(source="./assets/home.png")]
private var home_icon:Class;

[Embed(source="./assets/closeButton.png")]
private var close_icon:Class;


private function initTabs():void
{
	addTab("Home", nav, "This tab can't be closed", home_icon);
	
	for (var i:int = 0; i < 3; i++)
	{
		addTab("Tab " + (i + 1), nav);
	}
	
	callLater(initNonClosableTab);
}

private function initNonClosableTab():void
{
	nav.setClosePolicyForTab(0, SuperTab.CLOSE_NEVER);
}

private function addTab(lbl:String, navigator:SuperTabNavigator, contentString:String=null, icon:Class=
						null):void
{
	if (lbl == "")
		lbl = "(Untitled)";
	
	var curNum:Number = nav.numChildren + 1;
	
	var child:VBox = new VBox();
	
	child.setStyle("closable", true);
	
	child.label = lbl;
	
	if (icon)
	{
		child.icon = icon;
	}
	else
	{
		child.icon = document_icon;
	}
	var label:Label = new Label();
	label.text = contentString == null ? "Content for: " + lbl : contentString;
	
	child.addChild(label);
	
	navigator.addChild(child);
}

/* The following two functions are a bit of a hack to try to get the
* tab navigator to refresh the display and resize all the tabs. When
* you change stuff like tabWidth (which is a style) then the tab
* navigator has a hard time re-laying out the tabs. Adding and
* removing a child can sometimes trigger it to re-layout the tabs.
* I don't know, but just don't change tabWdith or horizontalGap or whatever
* else at runtime, OK? Pick some values and stick with them.
*/
private function invalidateLater():void
{
	var child:Canvas = new Canvas();
	nav.addChild(child);
	nav.removeChild(child);
	callLater(invalidateNav);
	callLater(invalidateNav);
}

private function invalidateNav():void
{
	nav.invalidateDisplayList();
}

private function tabsReordered(event:TabReorderEvent):void
{
	var tabBar:SuperTabBar = event.currentTarget as SuperTabBar;
	tabBar.setChildIndex(tabBar.getChildAt(event.oldIndex), event.newIndex);
}

// Event handler for the Tree control change event. 
public function treeChanged(event:Event):void { 
	selectedNode=Tree(event.target).selectedItem as XML; 
}