# GWH_TM_MZSC
#Entity #Standard #MASTER

| # | Column Name | Data Type | Nullable | Description |
|---|------------|-----------|----------|-------------|
| 1 | DEL_FLG | CHAR(1) | N | Delete Flag / 論理削除フラグ |
| 2 | CRT_YMD | CHAR(8) | N | Create Date (UTC) / 作成日(標準時) |
| 3 | CRT_TIM | CHAR(6) | N | Create HMS (UTC) / 作成時分秒(標準時) |
| 4 | CRT_TMID | NVARCHAR2(200) | N | Create Term Id / 作成端末ID |
| 5 | CRT_USER | NVARCHAR2(100) | N | Create User Id / 作成ユーザID |
| 6 | CRT_PGM | NVARCHAR2(60) | N | Create Program Id / 作成プログラムID |
| 7 | CRT_TM_ZONE | NVARCHAR2(6) | N | Create User Time Zone / 作成ユーザタイムゾーン |
| 8 | CRT_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時） |
| 9 | CRT_L_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル） |
| 10 | UPD_YMD | CHAR(8) | N | Update Date (UTC) / 更新日(標準時) |
| 11 | UPD_TIM | CHAR(6) | N | Update HMS (UTC) / 更新時分秒(標準時) |
| 12 | UPD_TMID | NVARCHAR2(200) | N | Update Terminal ID / 更新端末ID |
| 13 | UPD_USER | NVARCHAR2(100) | N | Update User ID / 更新ユーザID |
| 14 | UPD_PGM | NVARCHAR2(60) | N | Update Program ID / 更新プログラムID |
| 15 | UPD_TM_ZONE | NVARCHAR2(6) | N | Update User Time Zone / 更新ユーザタイムゾーン |
| 16 | UPD_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時） |
| 17 | UPD_L_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| 18 | MZSC_ZIP_COD | NVARCHAR2(14) | N | Zip Code / 郵便番号 |
| 19 | MZSC_JIS_COD | NVARCHAR2(10) | Y | Jis Code / JIS |
| 20 | MZSC_ADR1 | NVARCHAR2(510) | Y | Address1 / 住所1 |
| 21 | MZSC_ADR2 | NVARCHAR2(510) | Y | Address2 / 住所2 |
| 22 | MZSC_ADR3 | NVARCHAR2(510) | Y | Address3 / 住所3 |
| 23 | MZSC_SRT1 | NVARCHAR2(8) | Y | Sorting1 / 仕分１ |
| 24 | MZSC_STR1_NAM | NVARCHAR2(40) | Y | Store Name1 / 店所名１ |
| 25 | MZSC_SRT2 | NVARCHAR2(8) | Y | Sorting2 / 仕分２ |
| 26 | MZSC_STR2_NAM | NVARCHAR2(40) | Y | Store Name2 / 店所名２ |
| 27 | MZSC_LND1 | NVARCHAR2(10) | Y | Landing1 / 着地１ |
| 28 | MZSC_ARV_STR1 | NVARCHAR2(40) | Y | Arrival Store1 / 着店１ |
| 29 | MZSC_RT_SRT1 | NVARCHAR2(8) | Y | Route Sorting1 / 路線仕分１ |
| 30 | MZSC_LND2 | NVARCHAR2(10) | Y | Landing2 / 着地２ |
| 31 | MZSC_ARV_STR2 | NVARCHAR2(40) | Y | Arrival Store2 / 着店２ |
| 32 | MZSC_RT_SRT2 | NVARCHAR2(8) | Y | Route Sorting2 / 路線仕分２ |
| 33 | MZSC_LND3 | NVARCHAR2(10) | Y | Landing3 / 着地３ |
| 34 | MZSC_ARV_STR3 | NVARCHAR2(40) | Y | Arrival Store3 / 着店３ |
| 35 | MZSC_RT_SRT3 | NVARCHAR2(8) | Y | Route Sorting3 / 路線仕分３ |
| 36 | MZSC_LND4 | NVARCHAR2(10) | Y | Landing4 / 着地４ |
| 37 | MZSC_ARV_STR4 | NVARCHAR2(40) | Y | Arrival Store4 / 着店４ |
| 38 | MZSC_RT_SRT4 | NVARCHAR2(8) | Y | Route Sorting4 / 路線仕分４ |
| 39 | MZSC_RMI_KND | NVARCHAR2(510) | Y | Remote Island Kind / 離島区分 |
| 40 | MZSC_LND1_COD | NVARCHAR2(10) | Y | Landing Code1 / 着地コード01 |
| 41 | MZSC_SRT1_COD | NVARCHAR2(12) | Y | Sorting Code1 / 仕分番号01 |
| 42 | MZSC_ARV_STR1_COD | NVARCHAR2(40) | Y | Arrival Store Code1 / 着店コード01 |
| 43 | MZSC_ARV_STR1_NAM | NVARCHAR2(40) | Y | Arrival Store Name1 / 着店名01 |
| 44 | MZSC_LND2_COD | NVARCHAR2(10) | Y | Landing Code2 / 着地コード02 |
| 45 | MZSC_SRT2_COD | NVARCHAR2(12) | Y | Sorting Code2 / 仕分番号02 |
| 46 | MZSC_ARV_STR2_COD | NVARCHAR2(40) | Y | Arrival Store Code2 / 着店コード02 |
| 47 | MZSC_ARV_STR2_NAM | NVARCHAR2(40) | Y | Arrival Store Name2 / 着店名02 |
| 48 | MZSC_LND3_COD | NVARCHAR2(10) | Y | Landing Code3 / 着地コード03 |
| 49 | MZSC_SRT3_COD | NVARCHAR2(12) | Y | Sorting Code3 / 仕分番号03 |
| 50 | MZSC_ARV_STR3_COD | NVARCHAR2(40) | Y | Arrival Store Code3 / 着店コード03 |
| 51 | MZSC_ARV_STR3_NAM | NVARCHAR2(40) | Y | Arrival Store Name3 / 着店名03 |
| 52 | MZSC_LND4_COD | NVARCHAR2(10) | Y | Landing Code4 / 着地コード04 |
| 53 | MZSC_SRT4_COD | NVARCHAR2(12) | Y | Sorting Code4 / 仕分番号04 |
| 54 | MZSC_ARV_STR4_COD | NVARCHAR2(40) | Y | Arrival Store Code4 / 着店コード04 |
| 55 | MZSC_ARV_STR4_NAM | NVARCHAR2(40) | Y | Arrival Store Name4 / 着店名04 |
| 56 | MZSC_LND5_COD | NVARCHAR2(10) | Y | Landing Code5 / 着地コード05 |
| 57 | MZSC_SRT5_COD | NVARCHAR2(12) | Y | Sorting Code5 / 仕分番号05 |
| 58 | MZSC_ARV_STR5_COD | NVARCHAR2(40) | Y | Arrival Store Code5 / 着店コード05 |
| 59 | MZSC_ARV_STR5_NAM | NVARCHAR2(40) | Y | Arrival Store Name5 / 着店名05 |
| 60 | MZSC_LND6_COD | NVARCHAR2(10) | Y | Landing Code6 / 着地コード06 |
| 61 | MZSC_SRT6_COD | NVARCHAR2(12) | Y | Sorting Code6 / 仕分番号06 |
| 62 | MZSC_ARV_STR6_COD | NVARCHAR2(40) | Y | Arrival Store Code6 / 着店コード06 |
| 63 | MZSC_ARV_STR6_NAM | NVARCHAR2(40) | Y | Arrival Store Name6 / 着店名06 |
| 64 | MZSC_LND7_COD | NVARCHAR2(10) | Y | Landing Code7 / 着地コード07 |
| 65 | MZSC_SRT7_COD | NVARCHAR2(12) | Y | Sorting Code7 / 仕分番号07 |
| 66 | MZSC_ARV_STR7_COD | NVARCHAR2(40) | Y | Arrival Store Code7 / 着店コード07 |
| 67 | MZSC_ARV_STR7_NAM | NVARCHAR2(40) | Y | Arrival Store Name7 / 着店名07 |
| 68 | MZSC_LND8_COD | NVARCHAR2(10) | Y | Landing Code8 / 着地コード08 |
| 69 | MZSC_SRT8_COD | NVARCHAR2(12) | Y | Sorting Code8 / 仕分番号08 |
| 70 | MZSC_ARV_STR8_COD | NVARCHAR2(40) | Y | Arrival Store Code8 / 着店コード08 |
| 71 | MZSC_ARV_STR8_NAM | NVARCHAR2(40) | Y | Arrival Store Name8 / 着店名08 |
| 72 | MZSC_LND9_COD | NVARCHAR2(10) | Y | Landing Code9 / 着地コード09 |
| 73 | MZSC_SRT9_COD | NVARCHAR2(12) | Y | Sorting Code9 / 仕分番号09 |
| 74 | MZSC_ARV_STR9_COD | NVARCHAR2(40) | Y | Arrival Store Code9 / 着店コード09 |
| 75 | MZSC_ARV_STR9_NAM | NVARCHAR2(40) | Y | Arrival Store Name9 / 着店名09 |
| 76 | MZSC_LND10_COD | NVARCHAR2(10) | Y | Landing Code10 / 着地コード10 |
| 77 | MZSC_SRT10_COD | NVARCHAR2(12) | Y | Sorting Code10 / 仕分番号10 |
| 78 | MZSC_ARV_STR10_COD | NVARCHAR2(40) | Y | Arrival Store Code10 / 着店コード10 |
| 79 | MZSC_ARV_STR10_NAM | NVARCHAR2(40) | Y | Arrival Store Name10 / 着店名10 |
| 80 | MZSC_LND11_COD | NVARCHAR2(10) | Y | Landing Code11 / 着地コード11 |
| 81 | MZSC_SRT11_COD | NVARCHAR2(12) | Y | Sorting Code11 / 仕分番号11 |
| 82 | MZSC_ARV_STR11_COD | NVARCHAR2(40) | Y | Arrival Store Code11 / 着店コード11 |
| 83 | MZSC_ARV_STR11_NAM | NVARCHAR2(40) | Y | Arrival Store Name11 / 着店名11 |
| 84 | MZSC_LND12_COD | NVARCHAR2(10) | Y | Landing Code12 / 着地コード12 |
| 85 | MZSC_SRT12_COD | NVARCHAR2(12) | Y | Sorting Code12 / 仕分番号12 |
| 86 | MZSC_ARV_STR12_COD | NVARCHAR2(40) | Y | Arrival Store Code12 / 着店コード12 |
| 87 | MZSC_ARV_STR12_NAM | NVARCHAR2(40) | Y | Arrival Store Name12 / 着店名12 |
| 88 | MZSC_LND13_COD | NVARCHAR2(10) | Y | Landing Code13 / 着地コード13 |
| 89 | MZSC_SRT13_COD | NVARCHAR2(12) | Y | Sorting Code13 / 仕分番号13 |
| 90 | MZSC_ARV_STR13_COD | NVARCHAR2(40) | Y | Arrival Store Code13 / 着店コード13 |
| 91 | MZSC_ARV_STR13_NAM | NVARCHAR2(40) | Y | Arrival Store Name13 / 着店名13 |
| 92 | MZSC_LND14_COD | NVARCHAR2(10) | Y | Landing Code14 / 着地コード14 |
| 93 | MZSC_SRT14_COD | NVARCHAR2(12) | Y | Sorting Code14 / 仕分番号14 |
| 94 | MZSC_ARV_STR14_COD | NVARCHAR2(40) | Y | Arrival Store Code14 / 着店コード14 |
| 95 | MZSC_ARV_STR14_NAM | NVARCHAR2(40) | Y | Arrival Store Name14 / 着店名14 |
| 96 | MZSC_LND15_COD | NVARCHAR2(10) | Y | Landing Code15 / 着地コード15 |
| 97 | MZSC_SRT15_COD | NVARCHAR2(12) | Y | Sorting Code15 / 仕分番号15 |
| 98 | MZSC_ARV_STR15_COD | NVARCHAR2(40) | Y | Arrival Store Code15 / 着店コード15 |
| 99 | MZSC_ARV_STR15_NAM | NVARCHAR2(40) | Y | Arrival Store Name15 / 着店名15 |
| 100 | MZSC_LND16_COD | NVARCHAR2(10) | Y | Landing Code16 / 着地コード16 |
| 101 | MZSC_SRT16_COD | NVARCHAR2(12) | Y | Sorting Code16 / 仕分番号16 |
| 102 | MZSC_ARV_STR16_COD | NVARCHAR2(40) | Y | Arrival Store Code16 / 着店コード16 |
| 103 | MZSC_ARV_STR16_NAM | NVARCHAR2(40) | Y | Arrival Store Name16 / 着店名16 |
| 104 | MZSC_LND17_COD | NVARCHAR2(10) | Y | Landing Code17 / 着地コード17 |
| 105 | MZSC_SRT17_COD | NVARCHAR2(12) | Y | Sorting Code17 / 仕分番号17 |
| 106 | MZSC_ARV_STR17_COD | NVARCHAR2(40) | Y | Arrival Store Code17 / 着店コード17 |
| 107 | MZSC_ARV_STR17_NAM | NVARCHAR2(40) | Y | Arrival Store Name17 / 着店名17 |
| 108 | MZSC_LND18_COD | NVARCHAR2(10) | Y | Landing Code18 / 着地コード18 |
| 109 | MZSC_SRT18_COD | NVARCHAR2(12) | Y | Sorting Code18 / 仕分番号18 |
| 110 | MZSC_ARV_STR18_COD | NVARCHAR2(40) | Y | Arrival Store Code18 / 着店コード18 |
| 111 | MZSC_ARV_STR18_NAM | NVARCHAR2(40) | Y | Arrival Store Name18 / 着店名18 |
| 112 | MZSC_LND19_COD | NVARCHAR2(10) | Y | Landing Code19 / 着地コード19 |
| 113 | MZSC_SRT19_COD | NVARCHAR2(12) | Y | Sorting Code19 / 仕分番号19 |
| 114 | MZSC_ARV_STR19_COD | NVARCHAR2(40) | Y | Arrival Store Code19 / 着店コード19 |
| 115 | MZSC_ARV_STR19_NAM | NVARCHAR2(40) | Y | Arrival Store Name19 / 着店名19 |
| 116 | MZSC_LND20_COD | NVARCHAR2(10) | Y | Landing Code20 / 着地コード20 |
| 117 | MZSC_SRT20_COD | NVARCHAR2(12) | Y | Sorting Code20 / 仕分番号20 |
| 118 | MZSC_ARV_STR20_COD | NVARCHAR2(40) | Y | Arrival Store Code20 / 着店コード20 |
| 119 | MZSC_ARV_STR20_NAM | NVARCHAR2(40) | Y | Arrival Store Name20 / 着店名20 |
| 120 | MZSC_LND21_COD | NVARCHAR2(10) | Y | Landing Code21 / 着地コード21 |
| 121 | MZSC_SRT21_COD | NVARCHAR2(12) | Y | Sorting Code21 / 仕分番号21 |
| 122 | MZSC_ARV_STR21_COD | NVARCHAR2(40) | Y | Arrival Store Code21 / 着店コード21 |
| 123 | MZSC_ARV_STR21_NAM | NVARCHAR2(40) | Y | Arrival Store Name21 / 着店名21 |
| 124 | MZSC_LND22_COD | NVARCHAR2(10) | Y | Landing Code22 / 着地コード22 |
| 125 | MZSC_SRT22_COD | NVARCHAR2(12) | Y | Sorting Code22 / 仕分番号22 |
| 126 | MZSC_ARV_STR22_COD | NVARCHAR2(40) | Y | Arrival Store Code22 / 着店コード22 |
| 127 | MZSC_ARV_STR22_NAM | NVARCHAR2(40) | Y | Arrival Store Name22 / 着店名22 |
| 128 | MZSC_LND23_COD | NVARCHAR2(10) | Y | Landing Code23 / 着地コード23 |
| 129 | MZSC_SRT23_COD | NVARCHAR2(12) | Y | Sorting Code23 / 仕分番号23 |
| 130 | MZSC_ARV_STR23_COD | NVARCHAR2(40) | Y | Arrival Store Code23 / 着店コード23 |
| 131 | MZSC_ARV_STR23_NAM | NVARCHAR2(40) | Y | Arrival Store Name23 / 着店名23 |
| 132 | MZSC_LND24_COD | NVARCHAR2(10) | Y | Landing Code24 / 着地コード24 |
| 133 | MZSC_SRT24_COD | NVARCHAR2(12) | Y | Sorting Code24 / 仕分番号24 |
| 134 | MZSC_ARV_STR24_COD | NVARCHAR2(40) | Y | Arrival Store Code24 / 着店コード24 |
| 135 | MZSC_ARV_STR24_NAM | NVARCHAR2(40) | Y | Arrival Store Name24 / 着店名24 |
| 136 | MZSC_LND25_COD | NVARCHAR2(10) | Y | Landing Code25 / 着地コード25 |
| 137 | MZSC_SRT25_COD | NVARCHAR2(12) | Y | Sorting Code25 / 仕分番号25 |
| 138 | MZSC_ARV_STR25_COD | NVARCHAR2(40) | Y | Arrival Store Code25 / 着店コード25 |
| 139 | MZSC_ARV_STR25_NAM | NVARCHAR2(40) | Y | Arrival Store Name25 / 着店名25 |
| 140 | MZSC_LND26_COD | NVARCHAR2(10) | Y | Landing Code26 / 着地コード26 |
| 141 | MZSC_SRT26_COD | NVARCHAR2(12) | Y | Sorting Code26 / 仕分番号26 |
| 142 | MZSC_ARV_STR26_COD | NVARCHAR2(40) | Y | Arrival Store Code26 / 着店コード26 |
| 143 | MZSC_ARV_STR26_NAM | NVARCHAR2(40) | Y | Arrival Store Name26 / 着店名26 |
| 144 | MZSC_LND27_COD | NVARCHAR2(10) | Y | Landing Code27 / 着地コード27 |
| 145 | MZSC_SRT27_COD | NVARCHAR2(12) | Y | Sorting Code27 / 仕分番号27 |
| 146 | MZSC_ARV_STR27_COD | NVARCHAR2(40) | Y | Arrival Store Code27 / 着店コード27 |
| 147 | MZSC_ARV_STR27_NAM | NVARCHAR2(40) | Y | Arrival Store Name27 / 着店名27 |
| 148 | MZSC_LND28_COD | NVARCHAR2(10) | Y | Landing Code28 / 着地コード28 |
| 149 | MZSC_SRT28_COD | NVARCHAR2(12) | Y | Sorting Code28 / 仕分番号28 |
| 150 | MZSC_ARV_STR28_COD | NVARCHAR2(40) | Y | Arrival Store Code28 / 着店コード28 |
| 151 | MZSC_ARV_STR28_NAM | NVARCHAR2(40) | Y | Arrival Store Name28 / 着店名28 |
| 152 | MZSC_LND29_COD | NVARCHAR2(10) | Y | Landing Code29 / 着地コード29 |
| 153 | MZSC_SRT29_COD | NVARCHAR2(12) | Y | Sorting Code29 / 仕分番号29 |
| 154 | MZSC_ARV_STR29_COD | NVARCHAR2(40) | Y | Arrival Store Code29 / 着店コード29 |
| 155 | MZSC_ARV_STR29_NAM | NVARCHAR2(40) | Y | Arrival Store Name29 / 着店名29 |
| 156 | MZSC_LND30_COD | NVARCHAR2(10) | Y | Landing Code30 / 着地コード30 |
| 157 | MZSC_SRT30_COD | NVARCHAR2(12) | Y | Sorting Code30 / 仕分番号30 |
| 158 | MZSC_ARV_STR30_COD | NVARCHAR2(40) | Y | Arrival Store Code30 / 着店コード30 |
| 159 | MZSC_ARV_STR30_NAM | NVARCHAR2(40) | Y | Arrival Store Name30 / 着店名30 |
| 160 | MZSC_LND31_COD | NVARCHAR2(10) | Y | Landing Code31 / 着地コード31 |
| 161 | MZSC_SRT31_COD | NVARCHAR2(12) | Y | Sorting Code31 / 仕分番号31 |
| 162 | MZSC_ARV_STR31_COD | NVARCHAR2(40) | Y | Arrival Store Code31 / 着店コード31 |
| 163 | MZSC_ARV_STR31_NAM | NVARCHAR2(40) | Y | Arrival Store Name31 / 着店名31 |
| 164 | MZSC_LND32_COD | NVARCHAR2(10) | Y | Landing Code32 / 着地コード32 |
| 165 | MZSC_SRT32_COD | NVARCHAR2(12) | Y | Sorting Code32 / 仕分番号32 |
| 166 | MZSC_ARV_STR32_COD | NVARCHAR2(40) | Y | Arrival Store Code32 / 着店コード32 |
| 167 | MZSC_ARV_STR32_NAM | NVARCHAR2(40) | Y | Arrival Store Name32 / 着店名32 |
| 168 | MZSC_LND33_COD | NVARCHAR2(10) | Y | Landing Code33 / 着地コード33 |
| 169 | MZSC_SRT33_COD | NVARCHAR2(12) | Y | Sorting Code33 / 仕分番号33 |
| 170 | MZSC_ARV_STR33_COD | NVARCHAR2(40) | Y | Arrival Store Code33 / 着店コード33 |
| 171 | MZSC_ARV_STR33_NAM | NVARCHAR2(40) | Y | Arrival Store Name33 / 着店名33 |
| 172 | MZSC_LND34_COD | NVARCHAR2(10) | Y | Landing Code34 / 着地コード34 |
| 173 | MZSC_SRT34_COD | NVARCHAR2(12) | Y | Sorting Code34 / 仕分番号34 |
| 174 | MZSC_ARV_STR34_COD | NVARCHAR2(40) | Y | Arrival Store Code34 / 着店コード34 |
| 175 | MZSC_ARV_STR34_NAM | NVARCHAR2(40) | Y | Arrival Store Name34 / 着店名34 |
| 176 | MZSC_LND35_COD | NVARCHAR2(10) | Y | Landing Code35 / 着地コード35 |
| 177 | MZSC_SRT35_COD | NVARCHAR2(12) | Y | Sorting Code35 / 仕分番号35 |
| 178 | MZSC_ARV_STR35_COD | NVARCHAR2(40) | Y | Arrival Store Code35 / 着店コード35 |
| 179 | MZSC_ARV_STR35_NAM | NVARCHAR2(40) | Y | Arrival Store Name35 / 着店名35 |
| 180 | MZSC_LND36_COD | NVARCHAR2(10) | Y | Landing Code36 / 着地コード36 |
| 181 | MZSC_SRT36_COD | NVARCHAR2(12) | Y | Sorting Code36 / 仕分番号36 |
| 182 | MZSC_ARV_STR36_COD | NVARCHAR2(40) | Y | Arrival Store Code36 / 着店コード36 |
| 183 | MZSC_ARV_STR36_NAM | NVARCHAR2(40) | Y | Arrival Store Name36 / 着店名36 |
| 184 | MZSC_LND37_COD | NVARCHAR2(10) | Y | Landing Code37 / 着地コード37 |
| 185 | MZSC_SRT37_COD | NVARCHAR2(12) | Y | Sorting Code37 / 仕分番号37 |
| 186 | MZSC_ARV_STR37_COD | NVARCHAR2(40) | Y | Arrival Store Code37 / 着店コード37 |
| 187 | MZSC_ARV_STR37_NAM | NVARCHAR2(40) | Y | Arrival Store Name37 / 着店名37 |
| 188 | MZSC_LND38_COD | NVARCHAR2(10) | Y | Landing Code38 / 着地コード38 |
| 189 | MZSC_SRT38_COD | NVARCHAR2(12) | Y | Sorting Code38 / 仕分番号38 |
| 190 | MZSC_ARV_STR38_COD | NVARCHAR2(40) | Y | Arrival Store Code38 / 着店コード38 |
| 191 | MZSC_ARV_STR38_NAM | NVARCHAR2(40) | Y | Arrival Store Name38 / 着店名38 |
| 192 | MZSC_LND39_COD | NVARCHAR2(10) | Y | Landing Code39 / 着地コード39 |
| 193 | MZSC_SRT39_COD | NVARCHAR2(12) | Y | Sorting Code39 / 仕分番号39 |
| 194 | MZSC_ARV_STR39_COD | NVARCHAR2(40) | Y | Arrival Store Code39 / 着店コード39 |
| 195 | MZSC_ARV_STR39_NAM | NVARCHAR2(40) | Y | Arrival Store Name39 / 着店名39 |
| 196 | MZSC_LND40_COD | NVARCHAR2(10) | Y | Landing Code40 / 着地コード40 |
| 197 | MZSC_SRT40_COD | NVARCHAR2(12) | Y | Sorting Code40 / 仕分番号40 |
| 198 | MZSC_ARV_STR40_COD | NVARCHAR2(40) | Y | Arrival Store Code40 / 着店コード40 |
| 199 | MZSC_ARV_STR40_NAM | NVARCHAR2(40) | Y | Arrival Store Name40 / 着店名40 |
| 200 | MZSC_LND41_COD | NVARCHAR2(10) | Y | Landing Code41 / 着地コード41 |
| 201 | MZSC_SRT41_COD | NVARCHAR2(12) | Y | Sorting Code41 / 仕分番号41 |
| 202 | MZSC_ARV_STR41_COD | NVARCHAR2(40) | Y | Arrival Store Code41 / 着店コード41 |
| 203 | MZSC_ARV_STR41_NAM | NVARCHAR2(40) | Y | Arrival Store Name41 / 着店名41 |
| 204 | MZSC_LND42_COD | NVARCHAR2(10) | Y | Landing Code42 / 着地コード42 |
| 205 | MZSC_SRT42_COD | NVARCHAR2(12) | Y | Sorting Code42 / 仕分番号42 |
| 206 | MZSC_ARV_STR42_COD | NVARCHAR2(40) | Y | Arrival Store Code42 / 着店コード42 |
| 207 | MZSC_ARV_STR42_NAM | NVARCHAR2(40) | Y | Arrival Store Name42 / 着店名42 |
| 208 | MZSC_LND43_COD | NVARCHAR2(10) | Y | Landing Code43 / 着地コード43 |
| 209 | MZSC_SRT43_COD | NVARCHAR2(12) | Y | Sorting Code43 / 仕分番号43 |
| 210 | MZSC_ARV_STR43_COD | NVARCHAR2(40) | Y | Arrival Store Code43 / 着店コード43 |
| 211 | MZSC_ARV_STR43_NAM | NVARCHAR2(40) | Y | Arrival Store Name43 / 着店名43 |
| 212 | MZSC_LND44_COD | NVARCHAR2(10) | Y | Landing Code44 / 着地コード44 |
| 213 | MZSC_SRT44_COD | NVARCHAR2(12) | Y | Sorting Code44 / 仕分番号44 |
| 214 | MZSC_ARV_STR44_COD | NVARCHAR2(40) | Y | Arrival Store Code44 / 着店コード44 |
| 215 | MZSC_ARV_STR44_NAM | NVARCHAR2(40) | Y | Arrival Store Name44 / 着店名44 |
| 216 | MZSC_LND45_COD | NVARCHAR2(10) | Y | Landing Code45 / 着地コード45 |
| 217 | MZSC_SRT45_COD | NVARCHAR2(12) | Y | Sorting Code45 / 仕分番号45 |
| 218 | MZSC_ARV_STR45_COD | NVARCHAR2(40) | Y | Arrival Store Code45 / 着店コード45 |
| 219 | MZSC_ARV_STR45_NAM | NVARCHAR2(40) | Y | Arrival Store Name45 / 着店名45 |
| 220 | MZSC_LND46_COD | NVARCHAR2(10) | Y | Landing Code46 / 着地コード46 |
| 221 | MZSC_SRT46_COD | NVARCHAR2(12) | Y | Sorting Code46 / 仕分番号46 |
| 222 | MZSC_ARV_STR46_COD | NVARCHAR2(40) | Y | Arrival Store Code46 / 着店コード46 |
| 223 | MZSC_ARV_STR46_NAM | NVARCHAR2(40) | Y | Arrival Store Name46 / 着店名46 |
| 224 | MZSC_LND47_COD | NVARCHAR2(10) | Y | Landing Code47 / 着地コード47 |
| 225 | MZSC_SRT47_COD | NVARCHAR2(12) | Y | Sorting Code47 / 仕分番号47 |
| 226 | MZSC_ARV_STR47_COD | NVARCHAR2(40) | Y | Arrival Store Code47 / 着店コード47 |
| 227 | MZSC_ARV_STR47_NAM | NVARCHAR2(40) | Y | Arrival Store Name47 / 着店名47 |
| 228 | MZSC_LND48_COD | NVARCHAR2(10) | Y | Landing Code48 / 着地コード48 |
| 229 | MZSC_SRT48_COD | NVARCHAR2(12) | Y | Sorting Code48 / 仕分番号48 |
| 230 | MZSC_ARV_STR48_COD | NVARCHAR2(40) | Y | Arrival Store Code48 / 着店コード48 |
| 231 | MZSC_ARV_STR48_NAM | NVARCHAR2(40) | Y | Arrival Store Name48 / 着店名48 |
| 232 | MZSC_LND49_COD | NVARCHAR2(10) | Y | Landing Code49 / 着地コード49 |
| 233 | MZSC_SRT49_COD | NVARCHAR2(12) | Y | Sorting Code49 / 仕分番号49 |
| 234 | MZSC_ARV_STR49_COD | NVARCHAR2(40) | Y | Arrival Store Code49 / 着店コード49 |
| 235 | MZSC_ARV_STR49_NAM | NVARCHAR2(40) | Y | Arrival Store Name49 / 着店名49 |
| 236 | MZSC_LND50_COD | NVARCHAR2(10) | Y | Landing Code50 / 着地コード50 |
| 237 | MZSC_SRT50_COD | NVARCHAR2(12) | Y | Sorting Code50 / 仕分番号50 |
| 238 | MZSC_ARV_STR50_COD | NVARCHAR2(40) | Y | Arrival Store Code50 / 着店コード50 |
| 239 | MZSC_ARV_STR50_NAM | NVARCHAR2(40) | Y | Arrival Store Name50 / 着店名50 |
