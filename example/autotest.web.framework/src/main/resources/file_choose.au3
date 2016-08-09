$Title = $CmdLine[1]
$FileAbsPath = $CmdLine[2]

While 1
	WinWaitActive($Title)
	if WinExists($Title) Then
		ControlGetFocus('$Title')
		ControlClick($Title, '', '[CLASS:Edit;INSTANCE:1]')
		While Not (ControlGetText($Title, '', '[CLASS:Edit;INSTANCE:1]') == $FileAbsPath)
			ControlSetText($Title, '', '[CLASS:Edit;INSTANCE:1]', $FileAbsPath)
		Wend
		ControlSend($Title, '', '[CLASS:Edit;INSTANCE:1]', @CR)
		ExitLoop
	EndIf
Wend
