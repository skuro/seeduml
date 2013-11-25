/**
 * Author: Carlo Sciolla
 * CodeMirror mode for PlantUML sources
 */

CodeMirror.defineMode("plantuml", function(){

    var styles = {
        // common ceremony
        startuml: 'hr',
        enduml  : 'hr',

        // mostly used types
        type    : 'builtin',
        keyword : 'operator',
        arrow   : 'quote',
        bracket : 'bracket',

        // advanced features
        preproc : 'qualifier',
        skin    : 'header',
        color   : 'variable-2',

        // user elements
        node    : 'variable',
        label   : 'variable-3',
        comment : 'comment',

        error   : 'invalidchar'
    };

    /**
     * Splits words by whitespaces and returns an object with a property
     * for each word found
     */
    function splitTokens(str) {
        var res    = {};
        var tokens = str.split(" ");
        for (var i = 0; i < tokens.length; ++i) {
            res[tokens[i]] = true;
        }

        return res;
    }

    var types = splitTokens("abstract actor agent artifact boundary class cloud component control database entity enum folder frame interface node object participant rect state storage usecase");

    var keywords = splitTokens("@enduml @startuml activate again also alt as autonumber bottom box break center create critical deactivate destroy down else end endif endwhile footbox footer fork group header hide if is left link loop namespace newpage note of on opt over package page par partition ref repeat return right rotate show skin skinparam start stop title top top to bottom direction up while");

    var preprocs = splitTokens("!define !endif !ifdef !ifndef !include !undef");

    var skinParams = splitTokens("Activity2FontColor Activity2FontName Activity2FontSize Activity2FontStyle ActivityArrow2FontColor ActivityArrow2FontName ActivityArrow2FontSize ActivityArrow2FontStyle ActivityArrowColor ActivityBackgroundColor ActivityBarColor ActivityBorderColor ActivityEndColor ActivityFontColor ActivityFontName ActivityFontSize ActivityFontStyle ActivityStartColor BackgroundColor CircledCharacterFontColor CircledCharacterFontName CircledCharacterFontSize CircledCharacterFontStyle CircledCharacterRadius ClassArrowColor ClassAttributeFontColor ClassAttributeFontName ClassAttributeFontSize ClassAttributeFontStyle ClassAttributeIconSize ClassBackgroundColor ClassBorderColor ClassFontColor ClassFontName ClassFontSize ClassFontStyle ClassStereotypeFontColor ClassStereotypeFontName ClassStereotypeFontSize ClassStereotypeFontStyle ComponentBackgroundColor ComponentBorderColor ComponentFontColor ComponentFontName ComponentFontSize ComponentFontStyle ComponentInterfaceBackgroundColor ComponentInterfaceBorderColor ComponentStereotypeFontColor ComponentStereotypeFontName ComponentStereotypeFontSize ComponentStereotypeFontStyle DefaultFontColor DefaultFontName DefaultFontSize DefaultFontStyle FooterFontColor FooterFontName FooterFontSize FooterFontStyle GenericArrowFontColor GenericArrowFontName GenericArrowFontSize GenericArrowFontStyle HeaderFontColor HeaderFontName HeaderFontSize HeaderFontStyle IconPackageBackgroundColor IconPackageColor IconPrivateBackgroundColor IconPrivateColor IconProtectedBackgroundColor IconProtectedColor IconPublicBackgroundColor IconPublicColor LegendBackgroundColor LegendBorderColor LegendFontColor LegendFontName LegendFontSize LegendFontStyle Monochrome NoteBackgroundColor NoteBorderColor NoteFontColor NoteFontName NoteFontSize NoteFontStyle ObjectArrowColor ObjectAttributeFontColor ObjectAttributeFontName ObjectAttributeFontSize ObjectAttributeFontStyle ObjectBackgroundColor ObjectBorderColor ObjectFontColor ObjectFontName ObjectFontSize ObjectFontStyle ObjectStereotypeFontColor ObjectStereotypeFontName ObjectStereotypeFontSize ObjectStereotypeFontStyle PackageBackgroundColor PackageBorderColor PackageFontColor PackageFontName PackageFontSize PackageFontStyle PartitionBackgroundColor PartitionBorderColor SequenceActorBackgroundColor SequenceActorBorderColor SequenceActorFontColor SequenceActorFontName SequenceActorFontSize SequenceActorFontStyle SequenceArrowColor SequenceArrowFontColor SequenceArrowFontName SequenceArrowFontSize SequenceArrowFontStyle SequenceBoxBackgroundColor SequenceBoxBorderColor SequenceBoxFontColor SequenceBoxFontName SequenceBoxFontSize SequenceBoxFontStyle SequenceDelayFontColor SequenceDelayFontName SequenceDelayFontSize SequenceDelayFontStyle SequenceDividerBackgroundColor SequenceDividerFontColor SequenceDividerFontName SequenceDividerFontSize SequenceDividerFontStyle SequenceGroupBackgroundColor SequenceGroupBorderColor SequenceGroupFontColor SequenceGroupFontName SequenceGroupFontSize SequenceGroupFontStyle SequenceGroupHeaderFontColor SequenceGroupHeaderFontName SequenceGroupHeaderFontSize SequenceGroupHeaderFontStyle SequenceLifeLineBackgroundColor SequenceLifeLineBorderColor SequenceParticipantBackgroundColor SequenceParticipantBorderColor SequenceParticipantFontColor SequenceParticipantFontName SequenceParticipantFontSize SequenceParticipantFontStyle SequenceReferenceBackgroundColor SequenceReferenceBorderColor SequenceReferenceFontColor SequenceReferenceFontName SequenceReferenceFontSize SequenceReferenceFontStyle SequenceReferenceHeaderBackgroundColor SequenceTitleFontColor SequenceTitleFontName SequenceTitleFontSize SequenceTitleFontStyle StateArrowColor StateAttributeFontColor StateAttributeFontName StateAttributeFontSize StateAttributeFontStyle StateBackgroundColor StateBorderColor StateEndColor StateFontColor StateFontName StateFontSize StateFontStyle StateStartColor StereotypeABackgroundColor StereotypeCBackgroundColor StereotypeEBackgroundColor StereotypeIBackgroundColor TitleFontColor TitleFontName TitleFontSize TitleFontStyle UsecaseActorBackgroundColor UsecaseActorBorderColor UsecaseActorFontColor UsecaseActorFontName UsecaseActorFontSize UsecaseActorFontStyle UsecaseActorStereotypeFontColor UsecaseActorStereotypeFontName UsecaseActorStereotypeFontSize UsecaseActorStereotypeFontStyle UsecaseArrowColor UsecaseBackgroundColor UsecaseBorderColor UsecaseFontColor UsecaseFontName UsecaseFontSize UsecaseFontStyle UsecaseStereotypeFontColor UsecaseStereotypeFontName UsecaseStereotypeFontSize UsecaseStereotypeFontStyle");

    var colors = splitTokens("AliceBlue AntiqueWhite Aqua Aquamarine Azure Beige Bisque Black BlanchedAlmond Blue BlueViolet Brown BurlyWood CadetBlue Chartreuse Chocolate Coral CornflowerBlue Cornsilk Crimson Cyan DarkBlue DarkCyan DarkGoldenRod DarkGray DarkGreen DarkGrey DarkKhaki DarkMagenta DarkOliveGreen DarkOrchid DarkRed DarkSalmon DarkSeaGreen DarkSlateBlue DarkSlateGray DarkSlateGrey DarkTurquoise DarkViolet Darkorange DeepPink DeepSkyBlue DimGray DimGrey DodgerBlue FireBrick FloralWhite ForestGreen Fuchsia Gainsboro GhostWhite Gold GoldenRod Gray Green GreenYellow Grey HoneyDew HotPink IndianRed Indigo Ivory Khaki Lavender LavenderBlush LawnGreen LemonChiffon LightBlue LightCoral LightCyan LightGoldenRodYellow LightGray LightGreen LightGrey LightPink LightSalmon LightSeaGreen LightSkyBlue LightSlateGray LightSlateGrey LightSteelBlue LightYellow Lime LimeGreen Linen Magenta Maroon MediumAquaMarine MediumBlue MediumOrchid MediumPurple MediumSeaGreen MediumSlateBlue MediumSpringGreen MediumTurquoise MediumVioletRed MidnightBlue MintCream MistyRose Moccasin NavajoWhite Navy OldLace Olive OliveDrab Orange OrangeRed Orchid PaleGoldenRod PaleGreen PaleTurquoise PaleVioletRed PapayaWhip PeachPuff Peru Pink Plum PowderBlue Purple Red RosyBrown RoyalBlue SaddleBrown Salmon SandyBrown SeaGreen SeaShell Sienna Silver SkyBlue SlateBlue SlateGray SlateGrey Snow SpringGreen SteelBlue Tan Teal Thistle Tomato Turquoise Violet Wheat White WhiteSmoke Yellow YellowGreen");

    var tests = {
        symbol:  /[a-zA-Z0-9]/,
        fwarrow: /[^>]/,
        bwarrow: /[^\s]/,
        label:   /[^\]]/
    };

    var parseStartEndUml = function(stream) {
        stream.eatWhile(tests.symbol);
        switch(stream.current()) {
        case "@startuml":
            return styles.startuml;
            break;

        case "@enduml":
            return styles.enduml;
            break;

        default:
            return styles.error;
            break;
        }

    };

    var parseBackwardArrow = function(stream) {
        stream.eatWhile(tests.bwarrow);
        return styles.arrow;
    };

    var parseForwardArrow = function(stream) {
        stream.eatWhile(tests.fwarrow);
        stream.next(); // disregard the first arrow char
        var ch = stream.peek();
        if(">" == ch) {
            stream.next();
        }

        return styles.arrow;
    };

    var parseLabel = function(stream) {
        stream.eatWhile(tests.label);
        return styles.comment;
    };

    return {

        startState: function() {
            return {
                mode:    false, // false | multiline
                current: null   // the current state, used for multiline
            };
        },

        token: function (stream, state) {

            // we don't style blanks
            if(stream.eatSpace()) {
                return null;
            }

            switch(state.mode) {

                case "multiline":
                var next, escaped = false;
                while((next = stream.next()) != null) {
                    if (next == "\"" && !escaped) {
                        state.mode = false;
                        break;
                    }

                    escaped = !escaped && next == "\\";
                }

                return styles.label;
                break;

                default:
                var ch = stream.next();
                if("@" == ch) {
                    return parseStartEndUml(stream);
                }

                if("-" == ch) {
                    state.mode = "arrow";
                    return parseForwardArrow(stream);
                }

                if("<" == ch) {
                    state.mode = "arrow";
                    return parseBackwardArrow(stream);
                }

                if("\"" == ch) {
                    state.mode = "multiline";
                    var next, escaped = false;
                    while((next = stream.next()) != null) {
                        if("\"" == next && !escaped) {
                            state.mode = false;
                            break;
                        }

                        escaped = !escaped && "\\" == next;
                    }

                    return styles.label;
                }

                if(":" == ch) {
                    stream.skipToEnd();
                    return styles.comment;
                }
                break;
            }
        }
    };

});
