package src;

/*
 * Module that contains variables needed by all other modules
 */

public class Variables {
    private boolean _json;
    private int _itemCount;
    private int _itemSize;
    private double _setRatio;
    private double _appendRatio;
    private int _appendSize;
    private double _prependRatio;
    private int _prependSize;
    private double _delRatio;
    private double _expRatio;
    private int _expiration;
    private int _addCount;

    public Variables(boolean _json2, int _itemCount2, int _itemSize2,
            double _setRatio2, double _appendRatio2, int _appendSize2,
            double _prependRatio2, int _prependSize2, double _delRatio2,
            double _expRatio2, int _expiration2, int _addCount2) {
        this._json = _json2;
        this._itemCount = _itemCount2;
        this._itemSize = _itemSize2;
        this._setRatio = _setRatio2;
        this._appendRatio = _appendRatio2;
        this._appendSize = _appendSize2;
        this._prependRatio = _prependRatio2;
        this._prependSize = _prependSize2;
        this._delRatio = _delRatio2;
        this._expRatio = _expRatio2;
        this._expiration = _expiration2;
        this._addCount = _addCount2;		
    }

    public boolean isJson() {
        return this._json;
    }

    public int getItemCount() {
        return this._itemCount;
    }

    public int getItemSize() {
        return this._itemSize;
    }

    public double getSetRatio() {
        return this._setRatio;
    }

    public double getAppendRatio() {
        return this._appendRatio;
    }

    public int getAppendSize() {
        return this._appendSize;
    }

    public double getPrependRatio() {
        return this._prependRatio;
    }

    public int getPrependSize() {
        return this._prependSize;
    }

    public double getDelRatio() {
        return this._delRatio;
    }

    public double getExpRatio() {
        return this._expRatio;
    }

    public int getExpiration() {
        return this._expiration;
    }

    public int getAddCount() {
        return this._addCount;
    }
}
