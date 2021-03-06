#!/usr/bin/env python

###############
# Constants
###############
BYTE_PER_GB = 1024*1024*1024.0
KB_PER_GB = 1024*1024.0
MB_PER_GB = 1024.0

MS_PER_SEC = 1000.0
SEC_PER_MIN = 60.0

ALGS = ('pagerank', 'sssp', 'wcc', 'mst')
ALG_PR, ALG_SSSP, ALG_WCC, ALG_MST = ALGS
ALG_PREMIZAN = 'premizan'

GRAPHS = ('livejournal', 'orkut', 'arabic', 'twitter', 'uk0705')
GRAPH_LJ, GRAPH_OR, GRAPH_AR, GRAPH_TW, GRAPH_UK = GRAPHS

MACHINES = ('16', '32', '64', '128')

SYSTEMS = ('giraph', 'gps', 'mizan', 'graphlab')
SYS_GIRAPH, SYS_GPS, SYS_MIZAN, SYS_GRAPHLAB = SYSTEMS

SYS_MODES = (('0','1'),      # Giraph: byte array, hash map
             ('0','1','2'),  # GPS: none, LALP, dynamic
             ('0',),         # Mizan: static
             ('0','1'))      # GraphLab: sync, async
SYSMODE_HASH = '1'           # premizan hash partitioning

# combination of all systems and their sys modes
ALL_SYS = [(system, sysmode)
            for system, sysmodes in zip(SYSTEMS, SYS_MODES)
            for sysmode in sysmodes]


# conversion modes
MODES = (0, 1, 2)
MODE_TIME, MODE_MEM, MODE_NET = MODES

# names for relevant statistics (indexed by "mode")
STATS = (('run', 'io', 'tot'),                  # time
         ('mem_min', 'mem_max', 'mem_avg'),     # memory
         ('recv_min', 'recv_max', 'recv_avg',   # net
          'sent_min', 'sent_max', 'sent_avg'))
