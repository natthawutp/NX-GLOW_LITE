# GWH_TJ_BJP
#Entity #Standard #BATCH

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
| 18 | BJP_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | BJP_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | BJP_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | BJP_PRSQ_NUM | NVARCHAR2(60) | N | Processing Seq / 処理シーケンス |
| 22 | BJP_LINE_NUM | NVARCHAR2(6) | N | Line Seq / 行シーケンス |
| 23 | BJP_BAT_COD | NVARCHAR2(14) | N | Batch ID / バッチID |
| 24 | BJP_PRM1 | NVARCHAR2(100) | Y | Search Parameter 1 / 業務パラメータ1 |
| 25 | BJP_PRM2 | NVARCHAR2(100) | Y | Search Parameter 2 / 業務パラメータ2 |
| 26 | BJP_PRM3 | NVARCHAR2(100) | Y | Search Parameter 3 / 業務パラメータ3 |
| 27 | BJP_PRM4 | NVARCHAR2(100) | Y | Search Parameter 4 / 業務パラメータ4 |
| 28 | BJP_PRM5 | NVARCHAR2(100) | Y | Search Parameter 5 / 業務パラメータ5 |
| 29 | BJP_PRM6 | NVARCHAR2(100) | Y | Search Parameter 6 / 業務パラメータ6 |
| 30 | BJP_PRM7 | NVARCHAR2(100) | Y | Search Parameter 7 / 業務パラメータ7 |
| 31 | BJP_PRM8 | NVARCHAR2(100) | Y | Search Parameter 8 / 業務パラメータ8 |
| 32 | BJP_PRM9 | NVARCHAR2(100) | Y | Search Parameter 9 / 業務パラメータ9 |
| 33 | BJP_PRM10 | NVARCHAR2(100) | Y | Search Parameter 10 / 業務パラメータ10 |
| 34 | BJP_PRM11 | NVARCHAR2(100) | Y | Search Parameter 11 / 業務パラメータ11 |
| 35 | BJP_PRM12 | NVARCHAR2(100) | Y | Search Parameter 12 / 業務パラメータ12 |
| 36 | BJP_PRM13 | NVARCHAR2(100) | Y | Search Parameter 13 / 業務パラメータ13 |
| 37 | BJP_PRM14 | NVARCHAR2(100) | Y | Search Parameter 14 / 業務パラメータ14 |
| 38 | BJP_PRM15 | NVARCHAR2(100) | Y | Search Parameter 15 / 業務パラメータ15 |
| 39 | BJP_PRM16 | NVARCHAR2(100) | Y | Search Parameter 16 / 業務パラメータ16 |
| 40 | BJP_PRM17 | NVARCHAR2(100) | Y | Search Parameter 17 / 業務パラメータ17 |
| 41 | BJP_PRM18 | NVARCHAR2(100) | Y | Search Parameter 18 / 業務パラメータ18 |
| 42 | BJP_PRM19 | NVARCHAR2(100) | Y | Search Parameter 19 / 業務パラメータ19 |
| 43 | BJP_PRM20 | NVARCHAR2(100) | Y | Search Parameter 20 / 業務パラメータ20 |
| 44 | BJP_PRM21 | NVARCHAR2(100) | Y | Search Parameter 21 / 業務パラメータ21 |
| 45 | BJP_PRM22 | NVARCHAR2(100) | Y | Search Parameter 22 / 業務パラメータ22 |
| 46 | BJP_PRM23 | NVARCHAR2(100) | Y | Search Parameter 23 / 業務パラメータ23 |
| 47 | BJP_PRM24 | NVARCHAR2(100) | Y | Search Parameter 24 / 業務パラメータ24 |
| 48 | BJP_PRM25 | NVARCHAR2(100) | Y | Search Parameter 25 / 業務パラメータ25 |
| 49 | BJP_PRM26 | NVARCHAR2(100) | Y | Search Parameter 26 / 業務パラメータ26 |
| 50 | BJP_PRM27 | NVARCHAR2(100) | Y | Search Parameter 27 / 業務パラメータ27 |
| 51 | BJP_PRM28 | NVARCHAR2(100) | Y | Search Parameter 28 / 業務パラメータ28 |
| 52 | BJP_PRM29 | NVARCHAR2(100) | Y | Search Parameter 29 / 業務パラメータ29 |
| 53 | BJP_PRM30 | NVARCHAR2(100) | Y | Search Parameter 30 / 業務パラメータ30 |
| 54 | BJP_PRM31 | NVARCHAR2(100) | Y | Search Parameter 31 / 業務パラメータ31 |
| 55 | BJP_PRM32 | NVARCHAR2(100) | Y | Search Parameter 32 / 業務パラメータ32 |
| 56 | BJP_PRM33 | NVARCHAR2(100) | Y | Search Parameter 33 / 業務パラメータ33 |
| 57 | BJP_PRM34 | NVARCHAR2(100) | Y | Search Parameter 34 / 業務パラメータ34 |
| 58 | BJP_PRM35 | NVARCHAR2(100) | Y | Search Parameter 35 / 業務パラメータ35 |
| 59 | BJP_PRM36 | NVARCHAR2(100) | Y | Search Parameter 36 / 業務パラメータ36 |
| 60 | BJP_PRM37 | NVARCHAR2(100) | Y | Search Parameter 37 / 業務パラメータ37 |
| 61 | BJP_PRM38 | NVARCHAR2(100) | Y | Search Parameter 38 / 業務パラメータ38 |
| 62 | BJP_PRM39 | NVARCHAR2(100) | Y | Search Parameter 39 / 業務パラメータ39 |
| 63 | BJP_PRM40 | NVARCHAR2(100) | Y | Search Parameter 40 / 業務パラメータ40 |
| 64 | BJP_PRM41 | NVARCHAR2(100) | Y | Search Parameter 41 / 業務パラメータ41 |
| 65 | BJP_PRM42 | NVARCHAR2(100) | Y | Search Parameter 42 / 業務パラメータ42 |
| 66 | BJP_PRM43 | NVARCHAR2(100) | Y | Search Parameter 43 / 業務パラメータ43 |
| 67 | BJP_PRM44 | NVARCHAR2(100) | Y | Search Parameter 44 / 業務パラメータ44 |
| 68 | BJP_PRM45 | NVARCHAR2(100) | Y | Search Parameter 45 / 業務パラメータ45 |
| 69 | BJP_PRM46 | NVARCHAR2(100) | Y | Search Parameter 46 / 業務パラメータ46 |
| 70 | BJP_PRM47 | NVARCHAR2(100) | Y | Search Parameter 47 / 業務パラメータ47 |
| 71 | BJP_PRM48 | NVARCHAR2(100) | Y | Search Parameter 48 / 業務パラメータ48 |
| 72 | BJP_PRM49 | NVARCHAR2(100) | Y | Search Parameter 49 / 業務パラメータ49 |
| 73 | BJP_PRM50 | NVARCHAR2(100) | Y | Search Parameter 50 / 業務パラメータ50 |
| 74 | BJP_PRM51 | NVARCHAR2(100) | Y | Search Parameter 51 / 業務パラメータ51 |
| 75 | BJP_PRM52 | NVARCHAR2(100) | Y | Search Parameter 52 / 業務パラメータ52 |
| 76 | BJP_PRM53 | NVARCHAR2(100) | Y | Search Parameter 53 / 業務パラメータ53 |
| 77 | BJP_PRM54 | NVARCHAR2(100) | Y | Search Parameter 54 / 業務パラメータ54 |
| 78 | BJP_PRM55 | NVARCHAR2(100) | Y | Search Parameter 55 / 業務パラメータ55 |
| 79 | BJP_PRM56 | NVARCHAR2(100) | Y | Search Parameter 56 / 業務パラメータ56 |
| 80 | BJP_PRM57 | NVARCHAR2(100) | Y | Search Parameter 57 / 業務パラメータ57 |
| 81 | BJP_PRM58 | NVARCHAR2(100) | Y | Search Parameter 58 / 業務パラメータ58 |
| 82 | BJP_PRM59 | NVARCHAR2(100) | Y | Search Parameter 59 / 業務パラメータ59 |
| 83 | BJP_PRM60 | NVARCHAR2(100) | Y | Search Parameter 60 / 業務パラメータ60 |
| 84 | BJP_PRM61 | NVARCHAR2(100) | Y | Search Parameter 61 / 業務パラメータ61 |
| 85 | BJP_PRM62 | NVARCHAR2(100) | Y | Search Parameter 62 / 業務パラメータ62 |
| 86 | BJP_PRM63 | NVARCHAR2(100) | Y | Search Parameter 63 / 業務パラメータ63 |
| 87 | BJP_PRM64 | NVARCHAR2(100) | Y | Search Parameter 64 / 業務パラメータ64 |
| 88 | BJP_PRM65 | NVARCHAR2(100) | Y | Search Parameter 65 / 業務パラメータ65 |
| 89 | BJP_PRM66 | NVARCHAR2(100) | Y | Search Parameter 66 / 業務パラメータ66 |
| 90 | BJP_PRM67 | NVARCHAR2(100) | Y | Search Parameter 67 / 業務パラメータ67 |
| 91 | BJP_PRM68 | NVARCHAR2(100) | Y | Search Parameter 68 / 業務パラメータ68 |
| 92 | BJP_PRM69 | NVARCHAR2(100) | Y | Search Parameter 69 / 業務パラメータ69 |
| 93 | BJP_PRM70 | NVARCHAR2(100) | Y | Search Parameter 70 / 業務パラメータ70 |
| 94 | BJP_PRM71 | NVARCHAR2(100) | Y | Search Parameter 71 / 業務パラメータ71 |
| 95 | BJP_PRM72 | NVARCHAR2(100) | Y | Search Parameter 72 / 業務パラメータ72 |
| 96 | BJP_PRM73 | NVARCHAR2(100) | Y | Search Parameter 73 / 業務パラメータ73 |
| 97 | BJP_PRM74 | NVARCHAR2(100) | Y | Search Parameter 74 / 業務パラメータ74 |
| 98 | BJP_PRM75 | NVARCHAR2(100) | Y | Search Parameter 75 / 業務パラメータ75 |
| 99 | BJP_PRM76 | NVARCHAR2(100) | Y | Search Parameter 76 / 業務パラメータ76 |
| 100 | BJP_PRM77 | NVARCHAR2(100) | Y | Search Parameter 77 / 業務パラメータ77 |
| 101 | BJP_PRM78 | NVARCHAR2(100) | Y | Search Parameter 78 / 業務パラメータ78 |
| 102 | BJP_PRM79 | NVARCHAR2(100) | Y | Search Parameter 79 / 業務パラメータ79 |
| 103 | BJP_PRM80 | NVARCHAR2(100) | Y | Search Parameter 80 / 業務パラメータ80 |
| 104 | BJP_DPRM1 | DATE | Y | Search Date Parameter 1 / 業務日付パラメータ1 |
| 105 | BJP_DPRM2 | DATE | Y | Search Date Parameter 2 / 業務日付パラメータ2 |
| 106 | BJP_DPRM3 | DATE | Y | Search Date Parameter 3 / 業務日付パラメータ3 |
| 107 | BJP_DPRM4 | DATE | Y | Search Date Parameter 4 / 業務日付パラメータ4 |
| 108 | BJP_DPRM5 | DATE | Y | Search Date Parameter 5 / 業務日付パラメータ5 |
| 109 | BJP_DPRM6 | DATE | Y | Search Date Parameter 6 / 業務日付パラメータ6 |
| 110 | BJP_DPRM7 | DATE | Y | Search Date Parameter 7 / 業務日付パラメータ7 |
| 111 | BJP_DPRM8 | DATE | Y | Search Date Parameter 8 / 業務日付パラメータ8 |
| 112 | BJP_DPRM9 | DATE | Y | Search Date Parameter 9 / 業務日付パラメータ9 |
| 113 | BJP_DPRM10 | DATE | Y | Search Date Parameter 10 / 業務日付パラメータ10 |
| 114 | BJP_NPRM1 | NUMBER(14,4) | Y | Search Number Parameter 1 / 業務数値パラメータ1 |
| 115 | BJP_NPRM2 | NUMBER(14,4) | Y | Search Number Parameter 2 / 業務数値パラメータ2 |
| 116 | BJP_NPRM3 | NUMBER(14,4) | Y | Search Number Parameter 3 / 業務数値パラメータ3 |
| 117 | BJP_NPRM4 | NUMBER(14,4) | Y | Search Number Parameter 4 / 業務数値パラメータ4 |
| 118 | BJP_NPRM5 | NUMBER(14,4) | Y | Search Number Parameter 5 / 業務数値パラメータ5 |
| 119 | BJP_NPRM6 | NUMBER(14,4) | Y | Search Number Parameter 6 / 業務数値パラメータ6 |
| 120 | BJP_NPRM7 | NUMBER(14,4) | Y | Search Number Parameter 7 / 業務数値パラメータ7 |
| 121 | BJP_NPRM8 | NUMBER(14,4) | Y | Search Number Parameter 8 / 業務数値パラメータ8 |
| 122 | BJP_NPRM9 | NUMBER(14,4) | Y | Search Number Parameter 9 / 業務数値パラメータ9 |
| 123 | BJP_NPRM10 | NUMBER(14,4) | Y | Search Number Parameter 10 / 業務数値パラメータ10 |
| 124 | BJP_NPRM11 | NUMBER(14,4) | Y | Search Number Parameter 11 / 業務数値パラメータ11 |
| 125 | BJP_NPRM12 | NUMBER(14,4) | Y | Search Number Parameter 12 / 業務数値パラメータ12 |
| 126 | BJP_NPRM13 | NUMBER(14,4) | Y | Search Number Parameter 13 / 業務数値パラメータ13 |
| 127 | BJP_NPRM14 | NUMBER(14,4) | Y | Search Number Parameter 14 / 業務数値パラメータ14 |
| 128 | BJP_NPRM15 | NUMBER(14,4) | Y | Search Number Parameter 15 / 業務数値パラメータ15 |
| 129 | BJP_NPRM16 | NUMBER(14,4) | Y | Search Number Parameter 16 / 業務数値パラメータ16 |
| 130 | BJP_NPRM17 | NUMBER(14,4) | Y | Search Number Parameter 17 / 業務数値パラメータ17 |
| 131 | BJP_NPRM18 | NUMBER(14,4) | Y | Search Number Parameter 18 / 業務数値パラメータ18 |
| 132 | BJP_NPRM19 | NUMBER(14,4) | Y | Search Number Parameter 19 / 業務数値パラメータ19 |
| 133 | BJP_NPRM20 | NUMBER(14,4) | Y | Search Number Parameter 20 / 業務数値パラメータ20 |
| 134 | BJP_NPRM21 | NUMBER(14,4) | Y | Search Number Parameter 21 / 業務数値パラメータ21 |
| 135 | BJP_NPRM22 | NUMBER(14,4) | Y | Search Number Parameter 22 / 業務数値パラメータ22 |
| 136 | BJP_NPRM23 | NUMBER(14,4) | Y | Search Number Parameter 23 / 業務数値パラメータ23 |
| 137 | BJP_NPRM24 | NUMBER(14,4) | Y | Search Number Parameter 24 / 業務数値パラメータ24 |
| 138 | BJP_NPRM25 | NUMBER(14,4) | Y | Search Number Parameter 25 / 業務数値パラメータ25 |
| 139 | BJP_RPRM1 | NVARCHAR2(1000) | Y | Search Remarks Parameter 101 / 業務備考パラメータ1 |
| 140 | BJP_RPRM2 | NVARCHAR2(1000) | Y | Search Remarks Parameter 102 / 業務備考パラメータ2 |
| 141 | BJP_RPRM3 | NVARCHAR2(1000) | Y | Search Remarks Parameter 103 / 業務備考パラメータ3 |
| 142 | BJP_RPRM4 | NVARCHAR2(1000) | Y | Search Remarks Parameter 104 / 業務備考パラメータ4 |
| 143 | BJP_RPRM5 | NVARCHAR2(1000) | Y | Search Remarks Parameter 105 / 業務備考パラメータ5 |
| 144 | BJP_PRM81 | NVARCHAR2(100) | Y | Search Parameter 81 / 業務パラメータ81 |
| 145 | BJP_PRM82 | NVARCHAR2(100) | Y | Search Parameter 82 / 業務パラメータ82 |
| 146 | BJP_PRM83 | NVARCHAR2(100) | Y | Search Parameter 83 / 業務パラメータ83 |
| 147 | BJP_PRM84 | NVARCHAR2(100) | Y | Search Parameter 84 / 業務パラメータ84 |
| 148 | BJP_PRM85 | NVARCHAR2(100) | Y | Search Parameter 85 / 業務パラメータ85 |
| 149 | BJP_PRM86 | NVARCHAR2(100) | Y | Search Parameter 86 / 業務パラメータ86 |
| 150 | BJP_PRM87 | NVARCHAR2(100) | Y | Search Parameter 87 / 業務パラメータ87 |
| 151 | BJP_PRM88 | NVARCHAR2(100) | Y | Search Parameter 88 / 業務パラメータ88 |
| 152 | BJP_PRM89 | NVARCHAR2(100) | Y | Search Parameter 89 / 業務パラメータ89 |
| 153 | BJP_PRM90 | NVARCHAR2(100) | Y | Search Parameter 90 / 業務パラメータ90 |
| 154 | BJP_PRM91 | NVARCHAR2(100) | Y | Search Parameter 91 / 業務パラメータ91 |
| 155 | BJP_PRM92 | NVARCHAR2(100) | Y | Search Parameter 92 / 業務パラメータ92 |
| 156 | BJP_PRM93 | NVARCHAR2(100) | Y | Search Parameter 93 / 業務パラメータ93 |
| 157 | BJP_PRM94 | NVARCHAR2(100) | Y | Search Parameter 94 / 業務パラメータ94 |
| 158 | BJP_PRM95 | NVARCHAR2(100) | Y | Search Parameter 95 / 業務パラメータ95 |
| 159 | BJP_PRM96 | NVARCHAR2(100) | Y | Search Parameter 96 / 業務パラメータ96 |
| 160 | BJP_PRM97 | NVARCHAR2(100) | Y | Search Parameter 97 / 業務パラメータ97 |
| 161 | BJP_PRM98 | NVARCHAR2(100) | Y | Search Parameter 98 / 業務パラメータ98 |
| 162 | BJP_PRM99 | NVARCHAR2(100) | Y | Search Parameter 99 / 業務パラメータ99 |
| 163 | BJP_PRM100 | NVARCHAR2(100) | Y | Search Parameter 100 / 業務パラメータ100 |
