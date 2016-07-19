$Title = $CmdLine[1]
$FileAbsPath = $CmdLine[2]

While 1
	WinWaitActive($Title)
	if WinExists($Title) Then
		ControlGetFocus('$Title')
		ControlClick($Title, '', '[CLASS:Edit;INSTANCE:1]')
		Send($FileAbsPath)
		Send(@CR)
		ExitLoop
	EndIf
Wend
