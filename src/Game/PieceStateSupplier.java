package Game;

import Pieces.PieceState;

import java.util.Optional;
import java.util.function.Function;

public interface PieceStateSupplier extends Function<int[], Optional<PieceState>>{}
