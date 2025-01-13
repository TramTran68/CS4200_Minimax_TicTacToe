#include "Utility.h"

std::string Utility::moveToString(const Move& move) {
    return std::string(1, 'A' + move.row) + std::to_string(move.col + 1);
}
