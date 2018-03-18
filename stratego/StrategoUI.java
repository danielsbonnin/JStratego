package stratego;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import stratego.board.BoardCoords;


import java.util.List;

/**
 * @author Daniel Bonnin
 */
public class StrategoUI {
    GridPane boardPane;
    List< List<Pane> > squares;
    List< List<Label> > squareLabels;
    List< List<String> > squareStyles;
    List<Button> setupButtons;
    Cursor mouseCursor;
    EventHandler<MouseEvent> sqClick;

    private int boardWidth;
    private int boardHeight;

    /**
     * Board coordinates at most recently clicked square
     */
    private static BoardCoords bc;

    /**
     * A value to attach change listeners to, to notify listeners of a new click
     */
    public static BooleanProperty newClick = new SimpleBooleanProperty(false);

    public StrategoUI(GridPane bp, List< Button > setupButtons, int w, int h) {
        this.boardPane = bp;
        this.squares = squares;
        this.setupButtons = setupButtons;
        this.boardWidth = w;
        this.boardHeight = h;

        this.sqClick = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {

                // clicked square
                Pane sq = (Pane)me.getTarget();

                // The clicked square's coordinates
                BoardCoords bc = new BoardCoords(GridPane.getRowIndex(sq), GridPane.getColumnIndex(sq));

                // set bc
                StrategoUI.setCoords(bc);

                // flip the truth value of newClick to notify change listeners
                StrategoUI.newClick.setValue(!StrategoUI.newClick.getValue());

                me.consume();
            }
        };
        this.boardPane.addEventFilter(MouseEvent.MOUSE_CLICKED, this.sqClick);
    }

    /**
     * Set the coordinates at the most recently clicked square
     * @param bc
     */
    public static void setCoords(BoardCoords bc) {
        StrategoUI.bc = bc;
    }

    /**
     * Get the coordinates at the most recently clicked square
     */
    public static BoardCoords getCoords() {
        return StrategoUI.bc;
    }

}
