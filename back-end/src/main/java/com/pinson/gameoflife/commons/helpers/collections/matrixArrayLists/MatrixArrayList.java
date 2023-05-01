package com.pinson.gameoflife.commons.helpers.collections.matrixArrayLists;

import com.pinson.gameoflife.commons.exceptions.NonPositiveValueException;
import com.pinson.gameoflife.commons.exceptions.NotFoundException;
import com.pinson.gameoflife.commons.helpers.collections.matrixArrayLists.exceptions.MatrixIndexOutOfBoundsException;
import com.pinson.gameoflife.commons.helpers.positions.MatrixPositions.IMatrixPosition;

import java.util.ArrayList;

/**
 * ArrayList of ArrayList.
 *
 * @param <T> Any Object
 */
public class MatrixArrayList<T> implements IMatrixArrayList<T> {
    private final ArrayList<ArrayList<T>> matrix;

    public MatrixArrayList() {
        this.matrix = new ArrayList<ArrayList<T>>();
    }

    /**
     * Instantiate a MatrixArrayList with a default size, values are null by default.
     *
     * @param rows int
     * @param columns int
     * @throws NonPositiveValueException When the given rows or columns are not a positive number.
     */
    public MatrixArrayList(int rows, int columns) throws NonPositiveValueException {
        if (rows <= 0)
            throw new NonPositiveValueException("Rows must be positive");
        if (columns <= 0)
            throw new NonPositiveValueException("Columns must be positive");

        this.matrix = new ArrayList<ArrayList<T>>();

        for (int i = 0; i < rows; i++) {
            ArrayList<T> column = new ArrayList<T>();

            for (int j = 0; j < columns; j++) {
                column.add(null);
            }

            this.matrix.add(column);
        }
    }

    /**
     * @return Number of rows of the matrix, as int.
     */
    @Override
    public int getRows() {
        return this.matrix.size();
    }

    /**
     * @return Number of columns of the matrix, as int.
     */
    @Override
    public int getColumns() {
        if (this.matrix.size() == 0)
            return 0;

        return this.matrix.get(0).size();
    }

    /**
     * Get an object stored in the matrix at specified indexes.
     *
     * @param row int
     * @param column int
     * @return The T typed object stored in the matrix.
     * @throws MatrixIndexOutOfBoundsException When the given indexes does not exist within the bounds of the matrix.
     */
    @Override
    public T get(int row, int column) throws MatrixIndexOutOfBoundsException {
        this.checkBounds(row, column);

        return this.matrix.get(row).get(column);
    }

    /**
     * Get an object stored in the matrix at specified MatrixPosition.
     *
     * @param position IMatrixPosition<Integer>
     * @return The T typed object stored in the matrix.
     * @throws MatrixIndexOutOfBoundsException When the given indexes does not exist within the bounds of the matrix.
     */
    @Override
    public T get(IMatrixPosition<Integer> position) throws MatrixIndexOutOfBoundsException {
        return this.get(position.getY(), position.getX());
    }

    /**
     * Set an object at specified indexes of the matrix.
     *
     * @param row int
     * @param column int
     * @param value Object to store, of type T.
     * @return The pointer of the matrix.
     * @throws MatrixIndexOutOfBoundsException When the given indexes does not exist within the bounds of the matrix.
     */
    @Override
    public IMatrixArrayList<T> set(int row, int column, T value) throws MatrixIndexOutOfBoundsException {
        this.checkBounds(row, column);
        this.matrix.get(row).set(column, value);

        return this;
    }

    /**
     * Set an object at specified MatrixPosition of the matrix.
     *
     * @param position IMatrixPosition<Integer>
     * @param value Object to store, of type T.
     * @return The pointer of the matrix.
     * @throws MatrixIndexOutOfBoundsException When the given indexes does not exist within the bounds of the matrix.
     */
    @Override
    public IMatrixArrayList<T> set(IMatrixPosition<Integer> position, T value) throws MatrixIndexOutOfBoundsException {
        return this.set(position.getY(), position.getX(), value);
    }

    /**
     * Remove an object at specified indexes of the matrix.
     *
     * @param row int
     * @param column int
     * @return The pointer of the matrix.
     * @throws MatrixIndexOutOfBoundsException When the given indexes does not exist within the bounds of the matrix.
     */
    @Override
    public IMatrixArrayList<T> remove(int row, int column) throws MatrixIndexOutOfBoundsException {
        this.checkBounds(row, column);
        this.matrix.get(row).remove(column);

        return this;
    }

    /**
     * Remove an object at specified MatrixPosition of the matrix.
     *
     * @param position IMatrixPosition<Integer>
     * @return The pointer of the matrix.
     * @throws MatrixIndexOutOfBoundsException When the given indexes does not exist within the bounds of the matrix.
     */
    @Override
    public IMatrixArrayList<T> remove(IMatrixPosition<Integer> position) throws MatrixIndexOutOfBoundsException {
        return this.remove(position.getY(), position.getX());
    }

    /**
     * Finds the MatrixPosition of a given T typed value.
     *
     * @param value Object of type T.
     * @return The MatrixPosition of the found value.
     * @throws NotFoundException When the given object doesn't exist within the matrix.
     */
    @Override
    public IMatrixPosition<Integer> find(T value) throws NotFoundException {
        int rowSize = this.matrix.size();
        for (int i = 0; i < rowSize; ++i) {
            ArrayList<T> column = this.matrix.get(i);
            int colSize = column.size();

            for (int j = 0; j < colSize; ++j) {
                if (column.get(j) == value) {
                    IMatrixPosition<Integer> position =  IMatrixPosition.create(j, i);
                    return position;
                }
            }
        }

        throw new NotFoundException();
    }

    @Override
    public IMatrixArrayList<T> resize(int rows, int columns) throws NonPositiveValueException {
        if (rows < 0 || columns < 0) throw new NonPositiveValueException();

        if (columns > this.getColumns())
            this.insertColumns(columns - this.getColumns());
        else if (columns < this.getColumns())
            this.removeColumns(this.getColumns() - columns);


        if (rows > this.getRows())
            this.insertRows(rows - this.getRows());
        else if (rows < this.getRows())
            this.removeRows(this.getRows() - rows);

        return this;
    }

    @Override
    public IMatrixArrayList<T> insertRows(int amount) throws NonPositiveValueException {
        int index = (this.getRows() > 0) ? this.getRows() - 1 : 0;

        try {
            this.insertRows(amount, index);
        } catch (MatrixIndexOutOfBoundsException e) {
            // this should never happen
        }

        return this;
    }

    @Override
    public IMatrixArrayList<T> insertRows(int amount, int index) throws NonPositiveValueException, MatrixIndexOutOfBoundsException {
        if (amount < 1) throw new NonPositiveValueException();
        this.checkRowBounds(index);


        for (int i = 0; i < amount; ++i) {
            ArrayList<T> row = new ArrayList<T>();

            for (int j = 0; j < this.getColumns(); ++j) {
                row.add(null);
            }

            this.matrix.add(index, row);
        }

        return this;
    }

    @Override
    public IMatrixArrayList<T> insertColumns(int amount) throws NonPositiveValueException {
        try {
            this.insertColumns(amount, this.getColumns());
        } catch (MatrixIndexOutOfBoundsException e) {
            // this should never happen
        }

        return this;
    }

    @Override
    public IMatrixArrayList<T> insertColumns(int amount, int index) throws NonPositiveValueException, MatrixIndexOutOfBoundsException {
        if (amount < 1) throw new NonPositiveValueException();
        this.checkBounds(0, index);

        for (int i = 0; i < this.getRows(); ++i) {
            ArrayList<T> row = this.matrix.get(i);

            for (int j = 0; j < amount; ++j) {
                row.add(index + j, null);
            }
        }

        return this;
    }

    @Override
    public IMatrixArrayList<T> removeRows(int amount) throws NonPositiveValueException {
        try {
            this.removeRows(amount, this.getRows());
        } catch (MatrixIndexOutOfBoundsException e) {
            // this should never happen
        }

        return this;
    }

    @Override
    public IMatrixArrayList<T> removeRows(int amount, int index) throws NonPositiveValueException, MatrixIndexOutOfBoundsException {
        if (amount < 1) throw new NonPositiveValueException();
        this.checkBounds(index, 0);

        for (int i = 0; i < amount; ++i) {
            this.matrix.remove(index);
        }

        return this;
    }

    @Override
    public IMatrixArrayList<T> removeColumns(int amount) throws NonPositiveValueException {
        try {
            this.removeColumns(amount, this.getColumns());
        } catch (MatrixIndexOutOfBoundsException e) {
            // this should never happen
        }

        return this;
    }

    @Override
    public IMatrixArrayList<T> removeColumns(int amount, int index) throws NonPositiveValueException, MatrixIndexOutOfBoundsException {
        int rowSize = this.getRows();

        if (amount < 1) throw new NonPositiveValueException();
        this.checkBounds(0, index);

        for (int i = 0; i < rowSize; ++i) {
            ArrayList<T> row = this.matrix.get(i);

            for (int j = 0; j < amount; ++j) {
                row.remove(index);
            }
        }

        return this;
    }

    /**
     * Checks if the given row and columns are within bounds of the matrix.
     *
     * @param row int
     * @param column int
     * @throws MatrixIndexOutOfBoundsException When the row and column are not within the bounds of the matrix.
     */
    protected void checkBounds(int row, int column) throws MatrixIndexOutOfBoundsException {
        this.checkRowBounds(row);
        this.checkColumnBounds(column);
    }

    /**
     * Checks if the given row is within bounds of the matrix.
     *
     * @param row int
     * @throws MatrixIndexOutOfBoundsException When the row is not within the bounds of the matrix.
     */
    protected void checkRowBounds(int row) throws MatrixIndexOutOfBoundsException {
        int rowSize = this.matrix.size();

        if (row < 0 || row >= rowSize)
            throw new MatrixIndexOutOfBoundsException("The row must be within the matrix bounds.");
    }

    /**
     * Checks if the given column is within bounds of the matrix.
     *
     * @param column int
     * @throws MatrixIndexOutOfBoundsException When the column is not within the bounds of the matrix.
     */
    protected void checkColumnBounds(int column) throws MatrixIndexOutOfBoundsException {
        int colSize = this.matrix.get(0).size();

        if (column < 0 || column >= colSize)
            throw new MatrixIndexOutOfBoundsException("The column must be within the matrix bounds.");
    }

    // Todo: clone method.

}